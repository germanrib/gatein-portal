/**
 * Copyright (C) 2009 eXo Platform SAS.
 * 
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 * 
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.exoplatform.web.application;

import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.gatein.portal.controller.resource.ResourceId;
import org.gatein.portal.controller.resource.ResourceScope;
import org.gatein.portal.controller.resource.script.FetchMap;
import org.gatein.portal.controller.resource.script.FetchMode;

import java.util.UUID;

/**
 * Created by The eXo Platform SAS
 * Mar 27, 2007  
 */
public class JavascriptManager
{
   Log log = ExoLogger.getLogger("portal:JavascriptManager");
   
   /** . */
   private FetchMap<ResourceId> resourceIds = new FetchMap<ResourceId>();
   
   /** . */
   private FetchMap<String> extendedScriptURLs = new FetchMap<String>();

   /** . */
   private StringBuilder scripts = new StringBuilder();

   /** . */
   private StringBuilder customizedOnloadJavascript = new StringBuilder();

   private RequireJS requireJS;

   public JavascriptManager()
   {
      requireJS = new RequireJS();
      requireJS.require("SHARED/base", "base");
   }

   /**
    * Add a valid javascript code
    * 
    * @param s a valid javascript code
    */
   public void addJavascript(CharSequence s)
   {
      if (s != null)
      {
         scripts.append(s.toString().trim());
         scripts.append(";\n");
      }
   }

   /**
    * Register a SHARE Javascript resource that will be loaded in Rendering phase
    * Script FetchMode is ON_LOAD by default
    */
   public void loadScriptResource(String name)
   {      
      loadScriptResource(ResourceScope.SHARED, name);
   }

   /**
    * Register a Javascript resource that will be loaded in Rendering phase
    * If mode is null, script will be loaded with mode defined in gatein-resources.xml
    */
   public void loadScriptResource(ResourceScope scope, String name)
   {
      if (scope == null)
      {
         throw new IllegalArgumentException("scope can't be null");
      }
      if (name == null)
      {
         throw new IllegalArgumentException("name can't be null");
      }
      resourceIds.add(new ResourceId(scope, name), null);
   }

   public FetchMap<ResourceId> getScriptResources()
   {
      return resourceIds;
   }
   
   public FetchMap<String> getExtendedScriptURLs()
   {
      return new FetchMap<String>(extendedScriptURLs);
   }

   public void addExtendedScriptURLs(String url)
   {
      this.extendedScriptURLs.add(url, FetchMode.IMMEDIATE);
   }

   public void addOnLoadJavascript(CharSequence s)
   {
      if (s != null)
      {
         String id = Integer.toString(Math.abs(s.hashCode()));
         StringBuilder script = new StringBuilder("base.Browser.addOnLoadCallback('mid");
         script.append("base.Browser.addOnLoadCallback('mid");
         script.append(id);
         script.append("',");
         script.append(s instanceof String ? (String)s : s.toString());
         script.append(");");
         requireJS.addScripts(script.toString());
      }
   }

   public void addOnResizeJavascript(CharSequence s)
   {
      if (s != null)
      {
         String id = Integer.toString(Math.abs(s.hashCode()));
         StringBuilder script = new StringBuilder();
         script.append("base.Browser.addOnResizeCallback('mid");
         script.append(id);
         script.append("',");
         script.append(s instanceof String ? (String)s : s.toString());
         script.append(");");
         requireJS.addScripts(script.toString());
      }
   }

   public void addOnScrollJavascript(CharSequence s)
   {
      if (s != null)
      {
         String id = Integer.toString(Math.abs(s.hashCode()));
         StringBuilder script = new StringBuilder();
         script.append("eXo.core.Browser.addOnScrollCallback('mid");
         script.append(id);
         script.append("',");
         script.append(s instanceof String ? (String)s : s.toString());
         script.append(");");
         requireJS.addScripts(script.toString());
      }
   }

   public void addCustomizedOnLoadScript(CharSequence s)
   {
      if (s != null)
      {
         customizedOnloadJavascript.append(s.toString().trim());
         customizedOnloadJavascript.append(";\n");
      }
   }

   /**
    * Returns javascripts which were added by {@link #addJavascript(CharSequence)},
    * {@link #addOnLoadJavascript(CharSequence)}, {@link #addOnResizeJavascript(CharSequence)},
    * {@link #addOnScrollJavascript(CharSequence)}, {@link #addCustomizedOnLoadScript(CharSequence)}
    * 
    * @return
    */
   public String getJavaScripts()
   {
      StringBuilder callback = new StringBuilder();
      callback.append(scripts);
      callback.append(customizedOnloadJavascript);
      return callback.toString();
   }

   public RequireJS require(String moduleId)
   {
      return require(moduleId, null);
   }

   public RequireJS require(String moduleId, String alias)
   {
      return requireJS.require(moduleId, alias);
   }
   
   public RequireJS getRequireJS()
   {
      return requireJS;
   }

   public String generateUUID()
   {
      return "uniq-" + UUID.randomUUID().toString();
   }
}
