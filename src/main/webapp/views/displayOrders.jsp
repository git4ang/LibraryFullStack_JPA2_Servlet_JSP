<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<a href="ang?mvc=addOrdered">
	<button type="button" class="btn btn-primary btn-block">ADD Ordered</button>
</a>

<table class="table table-striped table-hover">
	<thead>
	<tr>
		<th>ID ORDERED</th>
		<th>ID BOOK</th>
		<th>AUTHOR</th>
		<th>EDITOR</th>
		<th>ID SUPPLIER</th>
		<th>SOCIAL REASON</th>
		<th>CITY</th>
		<th>DATE PURCHASE</th>
		<th>PRICE</th>
		<th>NUMBER COPIES</th>
		<th colspan="2">ACTIONS</th>
	</tr>
	</thead>
	<tbody>
	<c:forEach items="${listOrders}" var="o">
		<tr>
			<td>${o.idOrdered}</td>
			<td>${o.idBook}</td>
			<td>${o.book.author}</td>
			<td>${o.book.editor}</td>
			<td>${o.idSupplier}</td>
			<td>${o.supplier.social_reason}</td>
			<td>${o.supplier.city}</td>
			<td>${o.datePurchase}</td>
			<td>${o.price}</td>
			<td>${o.nbr_copies}</td>
			<td>
				<a style="color: orange;" href="ang?mvc=updateOrdered&idOrdered=${o.idOrdered}">
					<button type="button" class="btn btn-primary">Update</button>
				</a>
			</td>
			<td>
				<a onclick="return confirm('Are you sure you want to DELETE this ORDERED: ${o.idOrdered} ?');"
				   href="ang?mvc=deleteOrdered&idOrdered=${o.idOrdered}">
					<button type="button" class="btn btn-danger">Delete</button>
				</a>
			</td>
		</tr>
	</c:forEach>
	</tbody>
</table>