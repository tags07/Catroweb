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

package at.tugraz.ist.catroweb.api;

import java.util.HashMap;

import static org.testng.AssertJUnit.*;

import org.openqa.selenium.By;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;

import at.tugraz.ist.catroweb.BaseTest;
import at.tugraz.ist.catroweb.common.*;

@Test(groups = { "api", "UploadTests" })
public class UploadTests extends BaseTest {
  
  @Test(groups = { "upload", "functionality" }, description = "overwrite already uploaded projects")
  public void uploadResubmission() throws Throwable {
    try {
      String title = "Resubmit this project";
      String response = projectUploader.upload(CommonData.getUploadPayload(title, "Resubmission test, overwrite already uploaded projects.", "test.zip", "583783a335bd40d3d0195a13432afabb", "", "", "", ""));
      String id = CommonFunctions.getValueFromJSONobject(response, "projectId");

      assertEquals("200", CommonFunctions.getValueFromJSONobject(response, "statusCode"));
      
      openLocation("details/" + id);
      assertTrue(containsElementText(By.id("projectDetailsProjectTitle"), title.toUpperCase()));
      assertTrue(isTextPresent("version: < 0.7.0beta"));

      //update the project
      response = projectUploader.upload(CommonData.getUploadPayload(title, "Resubmission test, overwrite already uploaded projects.", "test-0.7.0beta.catrobat", "e60affe0c115ba4e10474eab3efc47d6", "", "", "", ""));
      id = CommonFunctions.getValueFromJSONobject(response, "projectId");
      
      assertEquals("200", CommonFunctions.getValueFromJSONobject(response, "statusCode"));
      
      openLocation("details/" + id);
      assertTrue(containsElementText(By.id("projectDetailsProjectTitle"), title.toUpperCase()));
      assertTrue(isTextPresent("version: 0.7.0beta"));
    } catch(AssertionError e) {
      captureScreen("UploadTests.uploadResubmission");
      throw e;
    } catch(Exception e) {
      captureScreen("UploadTests.uploadResubmission");
      throw e;
    }
  }
  
  @Test(groups = { "upload", "functionality" }, description = "extract project title and description from xml")
  public void uploadXMLExtraction() throws Throwable {
    try {
      String projectTitle = "testTitle";
      String projectDescription = "testDescription";
      String response = projectUploader.upload(CommonData.getUploadPayload(projectTitle, projectDescription, "test-0.7.0beta-xml.catrobat", "a8fe01af9952cc570ca3efc7e7fd6e27", "", "", "", ""));
      String id = CommonFunctions.getValueFromJSONobject(response, "projectId");
      
      assertEquals("200", CommonFunctions.getValueFromJSONobject(response, "statusCode"));
      
      openLocation("details/" + id);
      assertFalse(containsElementText(By.id("projectDetailsProjectTitle"), projectTitle.toUpperCase()));
      assertFalse(isTextPresent(projectDescription));
      assertTrue(containsElementText(By.id("projectDetailsProjectTitle"), "XML-ProjectName".toUpperCase()));
      assertTrue(isTextPresent("XML-ProjectDescription"));
    } catch(AssertionError e) {
      captureScreen("UploadTests.uploadXMLExtraction");
      throw e;
    } catch(Exception e) {
      captureScreen("UploadTests.uploadXMLExtraction");
      throw e;
    }
  }
  
  @Test(dataProvider = "ftpProjectsForUpload", groups = { "upload", "functionality" }, description = "upload projects via ftp")
  public void uploadFtpProjects(HashMap<String, String> dataset) throws Throwable {
    try {
      String response = projectUploader.upload(dataset);
      assertEquals("200", CommonFunctions.getValueFromJSONobject(response, "statusCode"));
      openLocation();
      ajaxWait();
      assertProjectPresent(dataset.get("projectTitle"));
    } catch(AssertionError e) {
      captureScreen("UploadTests.uploadFtpProjects." + dataset.get("projectTitle"));
      throw e;
    } catch(Exception e) {
      captureScreen("UploadTests.uploadFtpProjects." + dataset.get("projectTitle"));
      throw e;
    }
  }
  
  @Test(dataProvider = "validProjectsForUpload", groups = { "upload", "functionality" }, description = "upload valid projects")
  public void uploadValidProjects(HashMap<String, String> dataset) throws Throwable {
    try {
      String response = projectUploader.upload(dataset);
      assertEquals("200", CommonFunctions.getValueFromJSONobject(response, "statusCode"));
      openLocation();
      ajaxWait();
      assertProjectPresent(dataset.get("projectTitle"));
    } catch(AssertionError e) {
      captureScreen("UploadTests.uploadValidProjects." + dataset.get("projectTitle"));
      throw e;
    } catch(Exception e) {
      captureScreen("UploadTests.uploadValidProjects." + dataset.get("projectTitle"));
      throw e;
    }
  }

  @Test(dataProvider = "invalidProjectsForUpload", groups = { "upload", "functionality" }, description = "upload invalid projects")
  public void uploadInvalidProjects(HashMap<String, String> dataset) throws Throwable {
    try {
      String response = projectUploader.upload(dataset);
      assertNotSame("200", CommonFunctions.getValueFromJSONobject(response, "statusCode"));
      openLocation();
      ajaxWait();
      assertProjectNotPresent(dataset.get("projectTitle"));
    } catch(AssertionError e) {
      captureScreen("UploadTests.uploadInvalidProjects." + dataset.get("projectTitle"));
      throw e;
    } catch(Exception e) {
      captureScreen("UploadTests.uploadInvalidProjects." + dataset.get("projectTitle"));
      throw e;
    }
  }

  @DataProvider(name = "ftpProjectsForUpload")
  public Object[][] ftpProjectsForUpload() {
    Object[][] returnArray = new Object[][] {
        { CommonData.getUploadFtpPayload("testing project upload", "Testing FTP uploads.", "test.zip", "583783a335bd40d3d0195a13432afabb", "", "", "", "") }
    };
    return returnArray;
  }
  
  @DataProvider(name = "validProjectsForUpload")
  public Object[][] validProjectsForUpload() {
    Object[][] returnArray = new Object[][] {
        { CommonData.getUploadPayload("testing project upload", "some description for my test project.", "test.zip", "583783a335bd40d3d0195a13432afabb", "", "", "", "") },
        { CommonData.getUploadPayload("my test project with spaces and some uppercases in fileChecksum", "some description for my test project.", "test.zip", "583783a335bd40d3d0195a13432afabb", "", "", "", "") },
        { CommonData.getUploadPayload("my spÄc1al c´har ' t3ßt pröjec+", "some description ' with -äöüÜÖÄß- for my test project.%&()[]{}_|~#", "test.zip", "583783a335bd40d3d0195a13432afabb", "", "", "", "") },
        { CommonData.getUploadPayload("my_test_project_with_looong_description", "some description for my test project. some description for my test project. some description for my test project. some description for my test project. some description for my test project. some description for my test project. some description for my test project. some description for my test project. ", "test.zip", "583783a335bd40d3d0195a13432afabb", "", "", "", "") },
        { CommonData.getUploadPayload("project with thumbnail", "this project has its own thumbnail inside the zip", "test2.zip", "c40c86d6c4407788fa723e1d9fade10e", "", "", "", "") },
        { CommonData.getUploadPayload("project v6 with thumbnail and xml-project extention", "this project has its own thumbnail and is v6 and has xml extention instead of spf", "test_version_6_xml.zip", "eefc4182b2497ac1d0204a1d5ccb320b", "", "", "", "") },
        { CommonData.getUploadPayload("project v8 to test the native app builder", "native app building test", "test_version_8_0.5.4a.catrobat", "d0b32588e6c23a0e19bb4f66eec85277", "", "", "", "") },
        { CommonData.getUploadPayload("new catroid extention", "this project has catroid as extention", "test.catrobat", "583783a335bd40d3d0195a13432afabb", "", "", "", "") }
    };
    return returnArray;
  }

  @DataProvider(name = "invalidProjectsForUpload")
  public Object[][] invalidProjectsForUpload() {
    Object[][] returnArray = new Object[][] {
        { CommonData.getUploadPayload("insulting word in description", "fuck the project!!!!", "test.zip", "583783A335BD40D3D0195A13432AFABB", "", "", "", "") },
        { CommonData.getUploadPayload("fucking word in title", "some description", "test.zip", "583783A335BD40D3D0195A13432AFABB", "", "", "", "") },
        { CommonData.getUploadPayload("no token given", "some description", "test.zip", "583783A335BD40D3D0195A13432AFABB", "", "", "", " ") },
        { CommonData.getUploadPayload("wrong checksum", "some description", "test.zip", "2c2d13d52cf670ea55b2014b336d1b4d", "", "", "", " ") },
        { CommonData.getUploadPayload("wrong token given", "some description", "test.zip", "583783A335BD40D3D0195A13432AFABB", "", "", "", "123") },
        { CommonData.getUploadPayload("invalid xml in project file", "some description", "invalid_xml.zip", "AADC2DEDE19CDB559E362DB2E119F038", "", "", "", " ") },
        { CommonData.getUploadPayload("invalid zip file", "some description", "not_a_zip.zip", "D1B761A18F525A2A20CAA2A5DA12BBF1", "", "", "", " ") } 
        };
    return returnArray;
  }
  
}
