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
import com.google.appengine.api.datastore.Key;
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

/** Servlet that deletes user comments from Datastore. */
@WebServlet("/delete-data")
public class DeleteDataServlet extends HttpServlet {
    
  /**
   * Delete user comments and return an empty response
   * @param request the request message
   * @param response the response message
   * @throws IOException if an I/O error occurs
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {    
    Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      Key commentKey = entity.getKey();
      datastore.delete(commentKey);

    }
    
    // response.setContentType("application/json;");
    // response.getWriter().println(convertToJsonUsingGson(""));
    response.sendRedirect("/index.html");
    
  }
  
  /**
   * Returns String input entered by the commenter
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

 /**
   * Returns the choice entered by the player, or 0 if the choice was out of the specified range. 
   * @return the user's input, or the default data for an input element
   * @param name of the input element or text area to retrieve user input from 
   * @param defaultValue to return if the user doesn't enter their own input
   */
  private int getUserMaximum(HttpServletRequest request) {
    // Get the input from the form. vs query string?
    String playerChoiceString = request.getParameter("commentlimit");

    // Convert the input to an int.
    int playerChoice;
    try {
      playerChoice = Integer.parseInt(playerChoiceString);
    } catch (NumberFormatException e) {
      System.err.println("Could not convert to int: " + playerChoiceString);
      return 0;
    }

    // Check that the input is between 1 and 10.
    if (playerChoice < 1 || playerChoice > 10) {
      System.err.println("Player choice is out of range: " + playerChoiceString);
      return 0;
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
