<?php
/*    Catroid: An on-device graphical programming language for Android devices
 *    Copyright (C) 2010-2011 The Catroid Team
 *    (<http://code.google.com/p/catroid/wiki/Credits>)
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Affero General Public License as
 *    published by the Free Software Foundation, either version 3 of the
 *    License, or (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Affero General Public License for more details.
 *
 *    You should have received a copy of the GNU Affero General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

require_once('frameworkTestsBootstrap.php');

class commonFunctionsTest extends PHPUnit_Framework_TestCase {

  public function testGetUsernameBlacklistArray() {
    $bl = getUsernameBlacklistArray();
    $this->assertTrue(is_array($bl));
    $this->assertTrue(in_array('admin', $bl));
    $this->assertTrue(in_array('catroid', $bl));
    $this->assertTrue(in_array('kittyroid', $bl));
  }

  public function testGetIpBlockClassWhitelistArray() {
    $wl = getIpBlockClassWhitelistArray();
    $this->assertTrue(is_array($wl));
    $this->assertTrue(in_array('privacypolicy', $wl));
    $this->assertTrue(in_array('terms', $wl));
    $this->assertTrue(in_array('copyrightpolicy', $wl));
    $this->assertTrue(in_array('imprint', $wl));
    $this->assertTrue(in_array('contactus', $wl));
    $this->assertTrue(in_array('errorPage', $wl));
  }

  public function testConvertBytesToMegabytes() {
    $this->assertEquals('&lt; 0.1', convertBytesToMegabytes(1));
    $this->assertEquals('&lt; 0.1', convertBytesToMegabytes(1000));
    $this->assertEquals('&lt; 0.1', convertBytesToMegabytes(10000));
    $this->assertEquals('&lt; 0.1', convertBytesToMegabytes(100000));
    $this->assertEquals(0.2, convertBytesToMegabytes(104900));    
    $this->assertEquals(1.2 , convertBytesToMegabytes(1234567));
    $this->assertEquals(9.6 , convertBytesToMegabytes(9999999));
  }
  
  public function testUnzipFile() {
    $zipFile = dirname(__FILE__).'/testdata/commonFunctionsTestData/test.zip';
    $destDir = dirname(__FILE__).'/testdata/commonFunctionsTestData/zipTest/';
    removeDir($destDir);
    $this->assertTrue(unzipFile($zipFile, $destDir));
    $this->assertTrue(is_dir($destDir));
    $this->assertTrue(is_dir($destDir.'images/'));
    $this->assertTrue(is_dir($destDir.'sounds/'));
    $this->assertTrue(is_file($destDir.'introducing catroid.spf'));
    $this->assertTrue(is_file($destDir.'images/1284732477347cat1-b.png'));
    removeDir($destDir);
    $zipFile = dirname(__FILE__).'/testdata/commonFunctionsTestData/not_a_zip.zip';
    $this->assertFalse(unzipFile($zipFile, $destDir));
    $this->assertFalse(is_dir($destDir));
    $this->assertFalse(is_dir($destDir.'images/'));
    $this->assertFalse(is_dir($destDir.'sounds/'));
    $this->assertFalse(is_file($destDir.'introducing catroid.spf'));
    $this->assertFalse(is_file($destDir.'images/1284732477347cat1-b.png'));
  }

  /**
   * @dataProvider randomLongStrings
   */
  public function testMakeShortString($string) {
    $this->assertEquals(20, strlen(makeShortString($string, 20)));
    $this->assertEquals(35, strlen(makeShortString($string, 35, '...mysuffix')));
  }

  public function testGetCatroidProjectQRCodeUrl() {
    @copy(dirname(__FILE__).'/testdata/test_qr.png', CORE_BASE_PATH.PROJECTS_QR_DIRECTORY.'test_qr'.PROJECTS_QR_EXTENTION);
    $this->assertTrue(is_string(getCatroidProjectQRCodeUrl('test_qr', 'test')));
    $this->assertTrue(is_string(getCatroidProjectQRCodeUrl('non_existing_id', 'non_existing')));
    @unlink(CORE_BASE_PATH.PROJECTS_QR_DIRECTORY.'test_qr'.PROJECTS_QR_EXTENTION);
    @unlink(CORE_BASE_PATH.PROJECTS_QR_DIRECTORY.'non_existing_id'.PROJECTS_QR_EXTENTION);
  }
  
  public function testGetAppProjectQRCodeUrl() {
    @copy(dirname(__FILE__).'/testdata/test_qr.png', CORE_BASE_PATH.PROJECTS_QR_DIRECTORY.'test_qr'.APP_QR_EXTENTION);
    $this->assertTrue(is_string(getAppProjectQRCodeUrl('test_qr', 'test')));
    $this->assertTrue(is_string(getAppProjectQRCodeUrl('non_existing_id', 'non_existing')));
    @unlink(CORE_BASE_PATH.PROJECTS_QR_DIRECTORY.'test_qr'.APP_QR_EXTENTION);
    @unlink(CORE_BASE_PATH.PROJECTS_QR_DIRECTORY.'non_existing_id'.APP_QR_EXTENTION);
  }
  
  public function testGenerateQRCode() {
    $projectId = 'another_non_existing_id';
    $urlToEncode = urlencode(BASE_PATH.'catroid/download/'.$projectId.PROJECTS_EXTENTION.'?fname='.urlencode($projectId));
    $destinationPath = CORE_BASE_PATH.PROJECTS_QR_DIRECTORY.$projectId.PROJECTS_QR_EXTENTION;
    $this->assertTrue(generateQRCode($urlToEncode, $destinationPath));
    @unlink(CORE_BASE_PATH.PROJECTS_QR_DIRECTORY.$projectId.PROJECTS_QR_EXTENTION);
  }

  public function testGetProjectThumbnailUrl() {
    $thumbSourceName = 'test_thumbnail.jpg';
    $thumbDestName = 'test_small.jpg';
    @unlink(CORE_BASE_PATH.PROJECTS_THUMBNAIL_DIRECTORY.$thumbDestName);
    $thumb = getProjectThumbnailUrl('test');
    $this->assertFalse(strpos($thumb, $thumbDestName));
    $this->assertTrue(is_int(strpos($thumb, PROJECTS_THUMBNAIL_DEFAULT.PROJECTS_THUMBNAIL_EXTENTION_SMALL)));
    copy(dirname(__FILE__).'/testdata/'.$thumbSourceName, CORE_BASE_PATH.PROJECTS_THUMBNAIL_DIRECTORY.$thumbDestName);
    $thumb = getProjectThumbnailUrl('test');
    $this->assertTrue(is_int(strpos($thumb, $thumbDestName)));
    @unlink(CORE_BASE_PATH.PROJECTS_THUMBNAIL_DIRECTORY.$thumbDestName);
  }

  public function testGetProjectImageUrl() {
    $thumbSourceName = 'test_thumbnail.jpg';
    $thumbDestName = 'test_large.jpg';
    @unlink(CORE_BASE_PATH.PROJECTS_THUMBNAIL_DIRECTORY.$thumbDestName);
    $thumb = getProjectImageUrl('test');
    $this->assertFalse(strpos($thumb, $thumbDestName));
    $this->assertTrue(is_int(strpos($thumb, PROJECTS_THUMBNAIL_DEFAULT.PROJECTS_THUMBNAIL_EXTENTION_LARGE)));
    copy(dirname(__FILE__).'/testdata/'.$thumbSourceName, CORE_BASE_PATH.PROJECTS_THUMBNAIL_DIRECTORY.$thumbDestName);
    $thumb = getProjectImageUrl('test');
    $this->assertTrue(is_int(strpos($thumb, $thumbDestName)));
    @unlink(CORE_BASE_PATH.PROJECTS_THUMBNAIL_DIRECTORY.$thumbDestName);
  }

  public function testGetTimeInWords() {
    require_once('frameworkTestModel.php');
    $testModel = new frameworkTestModel();

    $fromTime = time() - 10;
    $timeInWords = getTimeInWords($fromTime, $testModel->languageHandler, time());
    $this->assertTrue(is_string($timeInWords));
    $this->assertTrue(is_int(strpos($timeInWords, 'less')) && is_int(strpos($timeInWords, 'minute')));

    $fromTime = time() - 66;
    $timeInWords = getTimeInWords($fromTime, $testModel->languageHandler, time());
    $this->assertFalse(strpos($timeInWords, 'less'));
    $this->assertTrue(is_int(strpos($timeInWords, 'minute')));

    $fromTime = time() - 60*60*24-1;
    $timeInWords = getTimeInWords($fromTime, $testModel->languageHandler, time());
    $this->assertFalse(strpos($timeInWords, 'minute'));
    $this->assertTrue(is_int(strpos($timeInWords, 'day')));

    $fromTime = time() - 60*60*24*31-1;
    $timeInWords = getTimeInWords($fromTime, $testModel->languageHandler, time());
    $this->assertFalse(strpos($timeInWords, 'day'));
    $this->assertTrue(is_int(strpos($timeInWords, 'month')));

    $fromTime = time() - 60*60*24*32*12-1;
    $timeInWords = getTimeInWords($fromTime, $testModel->languageHandler, time());
    $this->assertFalse(strpos($timeInWords, 'month'));
    $this->assertTrue(is_int(strpos($timeInWords, 'year')));
  }
  
  public function testGetSupportedLanguagesArray() {
    require_once('frameworkTestModel.php');
    $testModel = new frameworkTestModel();
    $supportedLanguages = getSupportedLanguagesArray($testModel->languageHandler);
    $this->assertTrue(isset($supportedLanguages[SITE_DEFAULT_LANGUAGE]));
    $this->assertTrue($supportedLanguages[SITE_DEFAULT_LANGUAGE]['supported']);
  }
  
  public function testCopyAndRemoveDir() {
    $testFolder = dirname(__FILE__).'/testdata/commonFunctionsTestData/testfolder/';
    $runtimeFolder = dirname(__FILE__).'/testdata/commonFunctionsTestData/runtimeFolder/';
    copyDir($testFolder, $runtimeFolder);
    $this->assertTrue(is_dir($runtimeFolder));
    $this->assertTrue(is_dir($runtimeFolder.'subfolder1'));
    $this->assertTrue(is_dir($runtimeFolder.'subfolder2'));
    $this->assertTrue(is_dir($runtimeFolder.'subfolder2/subfolder1'));
    $this->assertTrue(is_file($runtimeFolder.'subfolder2/subfolder1/test.txt'));
    $this->assertEquals(md5_file($runtimeFolder.'subfolder2/subfolder1/test.txt'), md5_file($testFolder.'subfolder2/subfolder1/test.txt'));
    removeDir($runtimeFolder);
    $this->assertFalse(is_dir($runtimeFolder));
    $this->assertFalse(is_dir($runtimeFolder.'subfolder1'));
    $this->assertFalse(is_dir($runtimeFolder.'subfolder2'));
    $this->assertFalse(is_dir($runtimeFolder.'subfolder2/subfolder1'));
    $this->assertFalse(is_file($runtimeFolder.'subfolder2/subfolder1/test.txt'));     
  }


  public function randomLongStrings() {
    $returnArray = array();
    $strLen = 400;
    $chars = array('a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',' ');

    for($i=0;$i<5;$i++) {
      $str = '';
      for($j=0;$j<$strLen;$j++) {
        $str .= $chars[rand(0, count($chars)-1)];
      }
      $returnArray[$i] = array($str);
    }

    return $returnArray;
  }
}
?>