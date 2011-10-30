/*    Catroid: An on-device graphical programming language for Android devices
 *    Copyright (C) 2010-2011 The Catroid Team
 *    (<http://code.google.com/p/catroid/wiki/Credits>)
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Affero General Public License as
 *    published by the Free Software Foundation, either version 3 of the
 *    License, or (at your option) any later version.
 *
 *    An additional term exception under section 7 of the GNU Affero
 *    General Public License, version 3, is available at
 *    http://www.catroid.org/catroid/licenseadditionalterm
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Affero General Public License for more details.
 *
 *    You should have received a copy of the GNU Affero General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

var Menu = Class.$extend( {
  __include__ : [__baseClassVars],
  __init__ : function(userLogin_userId) {

    this.userLogin_userId = userLogin_userId;
    
    $("#menuForumButton").click({url:this.basePath + "addons/board",windowName:"board"}, jQuery.proxy(this.openWindow, this));
    $("#headerHomeButton").click({url:"catroid/index"}, jQuery.proxy(this.openLocation, this));
    $("#headerBackButton").click(jQuery.proxy(this.goBack, this));
    $("#menuWallButton").click({url:"catroid/wall"}, jQuery.proxy(this.openLocation, this));
    $("#menuSettingsButton").click({url:"catroid/settings"}, jQuery.proxy(this.openLocation, this));
    if(this.userLogin_userId == 0) {
      $("#menuWikiButton").click({url:this.basePath + "wiki",windowName:"wiki"}, jQuery.proxy(this.openWindow, this));
    } else {
      $("#menuWikiButton").click({url:this.basePath + "wiki/Main_Page?action=purge",windowName:"wiki"}, jQuery.proxy(this.openWindow, this));
    }

    $("#menuLoginButton").click($.proxy(this.toggleProfileBox, this));
    $("#menuRegistrationButton").click({url:"catroid/registration"}, jQuery.proxy(this.openLocation, this));
    $("#menuPasswordRecoveryButton").click({url:"catroid/passwordrecovery"}, jQuery.proxy(this.openLocation, this));
    $("#menuProfileButton").click({url:"catroid/profile"}, jQuery.proxy(this.openLocation, this));
    $("#menuWallButton").attr('disabled', true).removeClass('green').addClass('gray');
    $("#menuSettingsButton").attr('disabled', true).removeClass('rosy').addClass('gray');
    if(this.userLogin_userId > 0) {
      $("#firstRow").toggle(true);
      $("#secondRow").toggle(false);
      $("#thirdRow").toggle(false);
      $("#menuRegistrationButton").toggle(false);
      $("#menuProfileButton").attr('disabled', false).removeClass('gray').addClass('green');
    } else {
      $("#firstRow").toggle(true);
      $("#secondRow").toggle(true);
      $("#thirdRow").toggle(false);
      $("#menuRegistrationButton").toggle(true);
      $("#menuProfileButton").attr('disabled', true).removeClass('green').addClass('gray');
    }
    
    $("#forgotPassword").click($.proxy(this.toggleProfileBox, this));
    $("#headerLoginButton").click($.proxy(this.toggleProfileBox, this));
    $("#headerCancelButton").click($.proxy(this.toggleAllBoxes, this));
  },
  
  goBack : function(event) {
	  history.back();
  },
    
  openLocation : function(event) {
	  location.href = this.basePath+event.data.url;
  },
    
  openWindow : function(event) {
  	 window.open(event.data.url, event.data.windowName);
  },

  toggleProfileBox : function() {
    $("#normalHeaderButtons").toggle(false);
    $("#cancelHeaderButton").toggle(true);
    $("#headerProfileBox").toggle(true);
    if($("#headerLoginBox").css("display") == "block") {
      $("#loginUsername").focus();
    }
    scroll(0,0);
  },

  toggleAllBoxes : function() {
    $("#normalHeaderButtons").toggle(true);
    $("#cancelHeaderButton").toggle(false);
    $("#headerProfileBox").toggle(false);
  }
  
});
