<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<button type="button" class="btn btn-primary btn-block" data-toggle="modal" data-target="#addBook">ADD Book</button>
<table class="table table-striped table-hover">
	<thead>
		<tr>
			<th>ID Book</th>
			<th>ISBN</th>
			<th>TITLE</th>
			<th>GENRE</th>
			<th>NUMBER OF PAGES</th>
			<th>AUTHOR</th>
			<th>EDITOR</th>
			<th>PRICE</th>
			<c:if test="${! selectBook }">
				<th colspan="2">ACTIONS</th>
			</c:if>				
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${listBooks}" var="b">
			<tr>
				<c:if test="${ selectBook }">
					<td>
						<a href="orders?mvc=addOrdered&idBook=${b.idBook}">${b.idBook}</a>
					</td>
				</c:if>
				<c:if test="${ !selectBook }">
					<td style="color: red; font-weight: bold">${b.idBook}</td>
				</c:if>		
				<td>${b.isbn}</td>
				<td>${b.title}</td>
				<td>${b.theme}</td>
				<td>${b.nbr_pages}</td>
				<td>${b.author}</td>
				<td>${b.editor}</td>
				<td>${b.price}</td>
				<c:if test="${! selectBook }">
					<td>
						<a style="color: orange;" href="books?mvc=updateBook&idBook=${b.idBook}">
							<button type="button" class="btn btn-primary">Update</button>
						</a>
					</td>
					<td>
						<a onclick="return confirm('Are you sure you want to DELETE this Book: ${b.title} ?');"
									href="books?mvc=deleteBook&idBook=${b.idBook}">
							<button type="button" class="btn btn-danger">Delete</button>
						</a>
					</td>
				</c:if>
			</tr>
		</c:forEach>
	</tbody>
</table>

<div style="margin-left: 20%" class="modal container" id="addBook">
	<div class="modal-content">
		<div class="modal-body">
			<jsp:include page="addUpdateBook.jsp">
				<jsp:param value="${ updateBook }" name="updateBook"/>
			</jsp:include>
		</div>
	</div>
</div>
