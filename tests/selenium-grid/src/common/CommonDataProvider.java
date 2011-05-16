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

package at.tugraz.ist.catroweb.common;

import org.apache.tools.ant.taskdefs.Definer;

/**
 */
public class CommonDataProvider  {

	
	/** Description of getLoginUserDefault()
	 * 
	 * @return			default test username
	 */
	public static String getLoginUserDefault()
	{	
		return "catroweb";
	}
	
	 /** Description of getLoginPasswordDefault()
   * 
   * @return      default test passsword
   */
  public static String getLoginPasswordDefault()
  { 
    return "cat.roid.web";
  }	
}