<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Library</title>
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.5.1/dist/jquery.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.12.9/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">
</head>
<body>
    <div class="container">
        <h1 class="text-center text-info">MENU LIBRARY</h1>
        <nav class="navbar navbar-dark bg-dark navbar-fixed-top">
            <nav class="nav nav-pills nav-fill">
                <a class="nav-link active nav-item" href="#">byANG</a>
                <a class="nav-link nav-item" href="ang">Home</a>
                <div class="dropdown">
                    <a class="nav-link dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">Display</a>
                    <div class="dropdown-menu">
                        <a class="dropdown-item" href="ang?display=books">List of books</a>
                        <a class="dropdown-item" href="ang?display=suppliers">List of suppliers</a>
                        <a class="dropdown-item" href="ang?display=orders">List of orders</a>
                    </div>
                </div>
                <div class="dropdown">
                    <a class="nav-link dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">ADD</a>
                    <div class="dropdown-menu">
                        <a class="dropdown-item" href="ang?mvc=addBook">ADD a Book</a>
                        <a class="dropdown-item" href="ang?mvc=addSupplier">ADD a Supplier</a>
                        <a class="dropdown-item" href="ang?mvc=addOrdered">ADD a Ordered</a>
                    </div>
                </div>

                <c:if test="${ !empty listBooks || !empty listSuppliers || !empty listOrders }">
                    <form action="search?mvc=select" class="form-inline my-2 my-lg-0 ml-0">
                        <c:if test="${ ! empty listBooks }">
                            <select name="searchBy" id="book" class="form-control">
                                <option value="theme__boo">Theme</option>
                                <option value="author__boo">Author</option>
                                <option value="editor__boo">Editor</option>
                            </select>
                        </c:if>
                        <c:if test="${ ! empty listSuppliers }">
                            <select name="searchBy" id="supplier" class="form-control">
                                <option value="social_reason__sup">Social reason</option>
                                <option value="city__sup">City</option>
                                <option value="country__sup">Country</option>
                            </select>
                        </c:if>
                        <c:if test="${ ! empty listOrders }">
                            <select name="searchBy" id="ordered" class="form-control">
                                <option value="social_reason__ord">Social raison</option>
                                <option value="author__ord">Author</option>
                                <option value="editor__ord">Editor</option>
                            </select>
                        </c:if>
                        <input class="form-control mr-sm-2" name="searchedName" type="search" placeholder="Search..." aria-label="Search">
                        <input class="btn btn-outline-success my-2 my-sm-0" type="submit" value="Search">
                    </form>
                </c:if>
            </nav>
        </nav>

        <c:if test="${ not empty errorCrud}">
            <h5 style="color: red; background-color: black; text-align: center">${errorCrud}</h5>
        </c:if>

        <!-- display Book -->
        <c:if test="${ ! empty listBooks }">
            <jsp:include page="views/displayBooks.jsp">
                <jsp:param name="listBooks" value="${ listBooks }" />
            </jsp:include>
        </c:if>

        <!-- add or update Book -->
        <c:if test="${ empty listBooks && addBook || updateBook || !empty fieldsErrorsBook }">
            <jsp:include page="views/addUpdateBook.jsp">
                <jsp:param name="fieldsErrorsBook" value="${fieldsErrorsBook}"/>
                <jsp:param name="updateBook" value="${ updateBook }"/>
            </jsp:include>
        </c:if>

        <!-- display supplier -->
        <c:if test="${ ! empty listSuppliers }">
            <jsp:include page="views/displaySuppliers.jsp">
                <jsp:param name="listSuppliers" value="${ listSuppliers }" />
            </jsp:include>
        </c:if>

        <!-- add or update supplier -->
        <c:if test="${ empty listSuppliers && addSupplier || updateSupplier || !empty fieldsErrorsSupplier }">
            <jsp:include page="views/addUpdateSupplier.jsp">
                <jsp:param value="${ fieldsErrorsSupplier }" name="fieldsErrorsSupplier"/>
                <jsp:param value="${ updateSupplier }" name="updateSupplier"/>
            </jsp:include>
        </c:if>

        <!-- display ordered -->
        <c:if test="${ ! empty listOrders }">
            <jsp:include page="views/displayOrders.jsp">
                <jsp:param name="listOrders" value="${ listOrders }" />
            </jsp:include>
        </c:if>

        <!-- add ordered -->
        <c:if test="${ empty listOrders && addOrdered || updateOrdered || !empty fieldsErrorsOrdered }">
            <jsp:include page="views/addOrdered.jsp">
                <jsp:param value="${ fieldsErrorsOrdered }" name="fieldsErrorsOrdered"/>
                <jsp:param value="${ updateOrdered }" name="updateOrdered"/>
            </jsp:include>
        </c:if>
    </div>
</body>
</html>