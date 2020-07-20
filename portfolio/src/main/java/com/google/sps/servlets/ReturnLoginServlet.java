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

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
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

/** Servlet that returns login status of the user. */
@WebServlet("/login-status")
public class ReturnLoginServlet extends HttpServlet {

  /**
   * Fetches login status of current user and returns the status as a a boolean value.
   * Redirects user to login page if they are not logged in.
   * @param request the request message
   * @param response the response message
   * @throws IOException if an I/O error occurs
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    boolean loginStatus = false;
    UserService userService = UserServiceFactory.getUserService();
    response.setContentType("application/json;");
    
    if (userService.isUserLoggedIn()) {
        loginStatus = true;
    } else {
        String urlToRedirectToAfterUserLogsIn = "/login-status";
        String loginUrl = userService.createLoginURL(urlToRedirectToAfterUserLogsIn);
        response.sendRedirect(loginUrl);
    }
    
    response.getWriter().println(convertToJsonUsingGson(loginStatus));

  }

  /**
   * Converts a boolean into a JSON string using the Gson library.
   */
  private String convertToJsonUsingGson(boolean truthCondition) {
    Gson gson = new Gson();
    String json = gson.toJson(truthCondition);
    return json;
  }

}
