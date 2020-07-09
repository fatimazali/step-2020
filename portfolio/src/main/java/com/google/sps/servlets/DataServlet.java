// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/** Servlet that parses user comments, stores them in Datastore, and retrieves them from Datastore. */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);
    int maxComments = getUserMaximum(request); //("maxcomment", 20);
    FetchOptions commentLimit = FetchOptions.Builder.withLimit(maxComments);
    //List<Entity> resultsList = results.asList(FetchOptions.Builder.withLimit(maxComments));

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    List<Entity> resultsList = datastore.prepare(query).asList(commentLimit);
    

    ArrayList<String> comments = new ArrayList<>();
    int commentCount = 0; 
    for (Entity entity : resultsList) {
      long id = entity.getKey().getId();
      String comment = (String) entity.getProperty("text");

      comments.add(comment);
      
    }

    Gson gson = new Gson();

    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(comments));
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {    
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    // Get the input from the form.
    ArrayList<String> comments = new ArrayList<String>();
    String userComments = getParameter(request, "text-input", "");
    String[] words = userComments.split("\\s*,\\s*");
    
    for (String comment : words) {
        long timestamp = System.currentTimeMillis();
        Entity commentEntity = new Entity("Comment");
        commentEntity.setProperty("text", comment);
        commentEntity.setProperty("timestamp", timestamp);        

        datastore.put(commentEntity);
    }

    response.sendRedirect("/index.html");
  }
  
  /**
   * @return the user's input, or the default data for an input element
   * @param name of the input element or text area to retrieve user input from 
   * @param defaultValue to return if the user doesn't enter their own input
   */
  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }

//   /**
//    * @return the user's input, or the default data for an input element
//    * @param name of the input element or text area to retrieve user input from 
//    * @param defaultValue to return if the user doesn't enter their own input
//    */
//   private Integer getUserMaximum(HttpServletRequest request, String name, Integer defaultValue) {
//     Integer value = (long)request.getParameter(name);
//     if (value == null) {
//       return defaultValue;
//     }
//     return value;
//   }


  /** Returns the choice entered by the player, or -1 if the choice was invalid. */
  private int getUserMaximum(HttpServletRequest request) {
    // Get the input from the form.
    String playerChoiceString = request.getParameter("maxcomment");

    // Convert the input to an int.
    int playerChoice;
    try {
      playerChoice = Integer.parseInt(playerChoiceString);
    } catch (NumberFormatException e) {
      System.err.println("Could not convert to int: " + playerChoiceString);
      return -1;
    }

    // Check that the input is between 1 and 3.
    if (playerChoice < 1 || playerChoice > 3) {
      System.err.println("Player choice is out of range: " + playerChoiceString);
      return -1;
    }

    return playerChoice;
  }



   /**
   * Converts an ArrayList<String> into a JSON string using the Gson library. Note: We first added
   * the Gson library dependency to pom.xml.
   */
  private String convertToJsonUsingGson(ArrayList<String> locations) {
    Gson gson = new Gson();
    String json = gson.toJson(locations);
    return json;
  }

}
