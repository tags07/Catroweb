<?php
/*
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

?>

      <article>
        <div id="programmOfTheWeek">
          <header><?php echo $this->languageHandler->getString('recommended'); ?></header>
          <div>
            <a href="<?php echo BASE_PATH?>details/1"><img src="<?php echo BASE_PATH; ?>images/symbols/hippo.png" /></a>
            <div>
              <span>The Lila Hippo</span><br/>
              <span>von User123</span>
            </div>
          </div>
        </div>

        <header><?php echo $this->languageHandler->getString('newestProjects'); ?></header>
        <div id="newestProjects" class="projectContainer"></div>
        <div id="newestProjectsLoader" class="projectLoader"><img src="<?php echo BASE_PATH; ?>images/symbols/ajax-loader-bright.gif" /></div>
        <div id="newestShowMore" class="moreButton">
          <div class="img-load-more"></div>
          <p><?php echo $this->languageHandler->getString('showMore'); ?></p>
        </div> 
        <div class="projectContainerFooter"></div>

        <header><?php echo $this->languageHandler->getString('mostDownloaded'); ?></header>
        <div id="mostDownloadedProjects" class="projectContainer"></div>
        <div id="mostDownloadedProjectsLoader" class="projectLoader"><img src="<?php echo BASE_PATH; ?>images/symbols/ajax-loader-bright.gif" /></div>
        <div id="mostDownloadedShowMore" class="moreButton">
          <div class="img-load-more"></div>
          <p><?php echo $this->languageHandler->getString('showMore'); ?></p>
        </div>
        <div class="projectContainerFooter"></div>

        <header><?php echo $this->languageHandler->getString('mostViewed'); ?></header>
        <div id="mostViewedProjects" class="projectContainer"></div>
        <div id="mostViewedProjectsLoader" class="projectLoader"><img src="<?php echo BASE_PATH; ?>images/symbols/ajax-loader-bright.gif" /></div>
        <div id="mostViewedShowMore" class="moreButton">
          <div class="img-load-more"></div>
          <p><?php echo $this->languageHandler->getString('showMore'); ?></p>
        </div>
        <div class="projectContainerFooter"></div>

      </article>
      <script type="text/javascript">
        $(document).ready(function() {
          ProjectObject(<?php echo $this->newestProjectsParams; ?>).init();
          ProjectObject(<?php echo $this->mostDownloadedProjectsParams; ?>).init();
          ProjectObject(<?php echo $this->mostViewedProjectsParams; ?>).init();
        });
      </script>
