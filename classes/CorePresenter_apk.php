<?php
/**
  * Catroid: An on-device visual programming system for Android devices
  * Copyright (C) 2010-2013 The Catrobat Team
  * (<http://developer.catrobat.org/credits>)
  * 
  * This program is free software: you can redistribute it and/or modify
  * it under the terms of the GNU Affero General Public License as
  * published by the Free Software Foundation, either version 3 of the
  * License, or (at your option) any later version.
  * 
  * An additional term exception under section 7 of the GNU Affero
  * General Public License, version 3, is available at
  * http://developer.catrobat.org/license_additional_term
  * 
  * This program is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
  * GNU Affero General Public License for more details.
  * 
  * You should have received a copy of the GNU Affero General Public License
  * along with this program. If not, see <http://www.gnu.org/licenses/>.
  */

class CorePresenter_apk extends CorePresenterCommon {
  public function __construct(CoreModule $module) {
    parent::__construct($module);
  }

  public function display() {
    $data = $this->module->getData();
    $file = CORE_BASE_PATH.PROJECTS_DIRECTORY . $data['id'] . APP_EXTENSION;
    $filename = $data['file_name'];
    if(is_file($file)) {
      header("Content-type: application/vnd.android.package-archive");
      header('Content-Disposition: attachment; filename="' . urlencode(utf8_encode($filename)) . APP_EXTENSION . '"');
      readfile($file);
    } else {
      $this->module->errorHandler->showErrorPage('download', 'file_not_found', $file);
      exit();
    }
    return true;
  }

  public function __destruct() {
    parent::__destruct();
  }
}
?>
