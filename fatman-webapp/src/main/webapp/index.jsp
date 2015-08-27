<!DOCTYPE html>
<%--
Copyright (c) 2012 Google Inc.

Licensed under the Apache License, Version 2.0 (the "License"); you may not use
this file except in compliance with the License. You may obtain a copy of the
License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed
under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
CONDITIONS OF ANY KIND, either express or implied. See the License for the
specific language governing permissions and limitations under the License.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ page import="com.sundbybergsit.google.Util" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Logga in via Google+</title>
    <link rel="stylesheet" href="resources/css/style.css" type="text/css"/>
</head>
<body>
<header>
    <h1>Logga in via Google+</h1>

    <h2>Oooof. Just nu kämpar jag med att uppgradera alltihop så en del kanske inte funkar som det ska. Ha tålamod!</h2>
</header>

<div class="largerbox">
    <% if (Util.getFlow().loadCredential(session.getId()) == null) { %>
    <a class='login' href='connect'>Anslut!</a>
    <% } else { %>
    <% response.sendRedirect("start.jsf"); %>
    <% } %>
</div>
</body>
</html>
