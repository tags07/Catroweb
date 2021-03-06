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

class CorePreparePreparedStatements {
  protected static $instance = null;
  private $statements = array();
  protected $prepared = false;

  public static function getInstance() {
    if(self::$instance === null) {
      self::$instance = new CorePreparePreparedStatements();
    }
    return self::$instance;
  }

  public function prepare($connection) {
    $query = "SELECT 1 FROM pg_prepared_statements";
    $result = pg_query($connection, $query);
    if(!$this->prepared && !pg_num_rows($result)) {
      foreach($this->statements as $key => $value) {
        if($key && $value) {
          pg_prepare($connection, $key, $value) or die("Couldn't prepare statement: " . pg_last_error());
        }
      }

      $this->prepared = true;
    }
  }
  
  public function unPrepare() {
    $this->prepared = false;
    return;
  }

  public function setStatements($file) {
    if(file_exists($file)) {
      $xml = simplexml_load_file($file);
    } else {
      return false;
    }

    foreach($xml->children() as $query) {
      $attributes = $query->attributes();
      if($query->getName() && $attributes['name']) {
        $this->statements[strval($attributes['name'])] = strval($query);
      }
    }
    return true;
  }

  private function __construct() {

  }

  private function __clone() {
  }

  public function __destruct() {

  }
}

?>