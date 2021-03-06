/**
  *Catroid: An on-device visual programming system for Android devices
  *Copyright (C) 2010-2013 The Catrobat Team
  *(<http://developer.catrobat.org/credits>)
  *
  *This program is free software: you can redistribute it and/or modify
  *it under the terms of the GNU Affero General Public License as
  *published by the Free Software Foundation, either version 3 of the
  *License, or (at your option) any later version.
  *
  *An additional term exception under section 7 of the GNU Affero
  *General Public License, version 3, is available at
  *http://developer.catrobat.org/license_additional_term
  *
  *This program is distributed in the hope that it will be useful,
  *but WITHOUT ANY WARRANTY; without even the implied warranty of
  *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
  *GNU Affero General Public License for more details.
  *
  *You should have received a copy of the GNU Affero General Public License
  *along with this program. If not, see <http://www.gnu.org/licenses/>.
  */

package at.tugraz.ist.catroweb.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.sql.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.postgresql.Driver;
import org.testng.Reporter;

public class ProjectUploader {
  protected List<HashMap<String, String>> uploadedProjects;
  protected String webSite;

  public ProjectUploader(String webSite) {
    this.uploadedProjects = Collections.synchronizedList(new ArrayList<HashMap<String, String>>());
    this.webSite = webSite;
  }

  public synchronized void cleanup() {
    for(HashMap<String, String> item : this.uploadedProjects) {
      deleteProject(item.get("projectId"));
    }
    this.uploadedProjects.clear();
  }

  public void upload() {
    upload(new HashMap<String, String>());
  }

  public synchronized String upload(HashMap<String, String> payload) {
    HashMap<String, String> verifiedPayload = verifyPayload(payload);
    Charset utf8 = Charset.forName("UTF-8");
    HttpClient httpclient = new DefaultHttpClient();
    try {
      MultipartEntity reqEntity = new MultipartEntity();

      reqEntity.addPart("projectTitle", new StringBody(verifiedPayload.get("projectTitle"), utf8));
      reqEntity.addPart("projectDescription", new StringBody(verifiedPayload.get("projectDescription"), utf8));
      if(verifiedPayload.get("catroidFileName") != null) {
        String filename = verifiedPayload.get("catroidFileName");
        copyFile(Config.FILESYSTEM_BASE_PATH + Config.SELENIUM_GRID_TESTDATA + filename, Config.FILESYSTEM_TEMP_FOLDER + filename);
        reqEntity.addPart("catroidFileName", new StringBody(filename));
      } else {
        reqEntity.addPart("upload", new FileBody(new File(verifiedPayload.get("upload"))));
      }
      reqEntity.addPart("fileChecksum", new StringBody(verifiedPayload.get("fileChecksum"), utf8));
      reqEntity.addPart("userEmail", new StringBody(verifiedPayload.get("userEmail"), utf8));
      reqEntity.addPart("userLanguage", new StringBody(verifiedPayload.get("userLanguage"), utf8));
      reqEntity.addPart("username", new StringBody(verifiedPayload.get("username"), utf8));
      reqEntity.addPart("token", new StringBody(verifiedPayload.get("token"), utf8));

      HttpPost httppost = new HttpPost(this.webSite + Config.TESTS_BASE_PATH.substring(1) + "api/upload/upload.json");
      httppost.setEntity(reqEntity);
      HttpResponse response = httpclient.execute(httppost);
      HttpEntity resEntity = response.getEntity();

      if(resEntity != null) {
        String answer = EntityUtils.toString(resEntity);
        if(CommonFunctions.getValueFromJSONobject(answer, "statusCode").equals("200")) {
          String projectId = CommonFunctions.getValueFromJSONobject(answer, "projectId");
          verifiedPayload.put("projectId", projectId);
          this.uploadedProjects.add(verifiedPayload);
        }
        if(answer.equals("")) {
          System.out.println("Got empty answer from api/upload.php! Maybe there is a syntax error?!");
        }
        return answer;
      }
    } catch(Exception e) {
      System.out.println("Unknown Exception - upload failed! " + e.getMessage());
      return "";
    } finally {
      try {
        httpclient.getConnectionManager().shutdown();
      } catch(Exception ignore) {
      }
    }
    return "";
  }

  public String getProjectId(String key) {
    for(HashMap<String, String> item : this.uploadedProjects) {
      for(String value : item.values()) {
        if(value.equals(key))
          return item.get("projectId");
      }
    }
    return "";
  }

  public synchronized void remove(String key) {
    String projectId = getProjectId(key);
    if(projectId.equals("")) {
      projectId = key;
    }

    this.uploadedProjects.remove(getProject(projectId));
    deleteProject(projectId);
  }

  private HashMap<String, String> verifyPayload(HashMap<String, String> payload) {
    HashMap<String, String> data = new HashMap<String, String>();

    String projectTitle = Config.DEFAULT_UPLOAD_TITLE;
    if(payload.containsKey("projectTitle")) {
      projectTitle = payload.get("projectTitle");
    }
    data.put("projectTitle", projectTitle);

    String projectDescription = Config.DEFAULT_UPLOAD_DESCRIPTION;
    if(payload.containsKey("projectDescription")) {
      projectDescription = payload.get("projectDescription");
    }
    data.put("projectDescription", projectDescription);

    if(payload.containsKey("catroidFileName")) {
      data.put("catroidFileName", payload.get("catroidFileName"));
    } else {
      String upload = Config.DEFAULT_UPLOAD_FILE;
      if(payload.containsKey("upload")) {
        upload = payload.get("upload");
      }
      data.put("upload", upload);
    }

    String fileChecksum = Config.DEFAULT_UPLOAD_CHECKSUM;
    if(payload.containsKey("fileChecksum")) {
      fileChecksum = payload.get("fileChecksum");
    }
    data.put("fileChecksum", fileChecksum);

    String userEmail = Config.DEFAULT_UPLOAD_EMAIL;
    if(payload.containsKey("userEmail")) {
      userEmail = payload.get("userEmail");
    }
    data.put("userEmail", userEmail);

    String userLanguage = Config.DEFAULT_UPLOAD_LANGUAGE;
    if(payload.containsKey("userLanguage")) {
      userLanguage = payload.get("userLanguage");
    }
    data.put("userLanguage", userLanguage);

    String username = CommonData.getLoginUserDefault();
    if(payload.containsKey("username")) {
      username = payload.get("username");
    }
    data.put("username", username);
    
    String token = Config.DEFAULT_UPLOAD_TOKEN;
    if(payload.containsKey("token")) {
      token = payload.get("token");
    }
    data.put("token", token);

    return data;
  }

  private void deleteProject(String projectId) {
    if(!projectId.equals("")) {
      try {
        Driver driver = new Driver();
        DriverManager.registerDriver(driver);

        Connection connection = DriverManager.getConnection(Config.DB_HOST + Config.DB_NAME, Config.DB_USER, Config.DB_PASS);
        Statement statement = connection.createStatement();
        statement.executeUpdate("DELETE FROM projects WHERE id='" + projectId + "';");
        statement.close();
        connection.close();
        DriverManager.deregisterDriver(driver);
      } catch(SQLException e) {
        System.out.println("ProjectUploader: deleteProject: SQL Exception couldn't execute sql query!");
      }

      (new File(Config.FILESYSTEM_BASE_PATH + Config.PROJECTS_DIRECTORY + projectId + Config.PROJECTS_EXTENTION)).delete();
      (new File(Config.FILESYSTEM_BASE_PATH + Config.PROJECTS_QR_DIRECTORY + projectId + Config.PROJECTS_QR_EXTENTION)).delete();
      (new File(Config.FILESYSTEM_BASE_PATH + Config.PROJECTS_THUMBNAIL_DIRECTORY + projectId + Config.PROJECTS_THUMBNAIL_EXTENTION_ORIG)).delete();
      (new File(Config.FILESYSTEM_BASE_PATH + Config.PROJECTS_THUMBNAIL_DIRECTORY + projectId + Config.PROJECTS_THUMBNAIL_EXTENTION_SMALL)).delete();
      (new File(Config.FILESYSTEM_BASE_PATH + Config.PROJECTS_THUMBNAIL_DIRECTORY + projectId + Config.PROJECTS_THUMBNAIL_EXTENTION_LARGE)).delete();
      CommonFunctions.deleteDir(new File(Config.FILESYSTEM_BASE_PATH + Config.PROJECTS_UNZIPPED_DIRECTORY + projectId + Config.FILESYSTEM_SEPARATOR));
    } else {
      System.out.println("ProjectUploader: deleteProject: invalid project id:'" + projectId + "' - couldn't delete!");
    }
  }

  private HashMap<String, String> getProject(String key) {
    for(HashMap<String, String> item : this.uploadedProjects) {
      if(item.get("projectId").equals(key))
        return item;
    }
    return new HashMap<String, String>();
  }

  private void copyFile(String sourceFile, String destinationFile) {
    try {
      InputStream input = new FileInputStream(sourceFile);
      OutputStream output = new FileOutputStream(destinationFile);

      // Transfer bytes from in to out
      byte[] buffer = new byte[1024];
      int length;
      while((length = input.read(buffer)) > 0) {
        output.write(buffer, 0, length);
      }

      input.close();
      output.close();
    } catch(FileNotFoundException e) {
      Reporter.log("copyFile failed: no such file!");
      e.printStackTrace();
    } catch(IOException e) {
      Reporter.log("copyFile failed: io exception");
      e.printStackTrace();
    }
  }
}
