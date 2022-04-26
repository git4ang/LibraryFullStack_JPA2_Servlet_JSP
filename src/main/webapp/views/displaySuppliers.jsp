<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<button class="btn btn-primary btn-block" data-toggle="modal" data-target="#addSupplier">ADD Supplier</button>
<table class="table table-striped">
	<thead>
		<tr>
			<th>ID Supplier</th>
			<th>SOCIAL REASON</th>
			<th>CITY</th>
			<th>PHONE</th>
			<th>EMAIL</th>
			<c:if test="${ ! selectSupplier }">
				<th colspan="2">ACTIONS</th>
			</c:if>
		</tr>
	</thead>
	<tbody>
	<c:forEach items="${listSuppliers}" var="p">
		<tr>
			<c:if test="${ selectSupplier }">
				<td><a href="orders?mvc=addOrdered&idSupplier=${p.idSupplier}">${p.idSupplier}</a></td>
			</c:if>
			<c:if test="${ !selectSupplier }">
				<td style="color: red; font-weight: bold">${p.idSupplier}</td>
			</c:if>
			<td>${p.social_reason}</td>
			<td>${p.city}</td>
			<td>${p.phone}</td>
			<td>${p.email}</td>
			<c:if test="${ !selectSupplier }">
				<td>
					<a href="suppliers?mvc=updateSupplier&idSupplier=${p.idSupplier}">
						<button type="button" class="btn btn-primary">Update</button>	
					</a>
				</td>
				<td>
					<a style="color: red;" onclick="return confirm('Are you sure you want to DELETE this Supplier: ${p.social_reason} ?');"
								href="suppliers?mvc=deleteSupplier&idSupplier=${p.idSupplier}">
						<button type="button" class="btn btn-danger">Delete</button>
					</a>
				</td>
				</c:if>
			</tr>
		</c:forEach>
	</tbody>
</table>

<div style="margin-left: 20%" class="modal container" id="addSupplier">
	<div class="modal-content">
		<div class="modal-body">
			<jsp:include page="addUpdateSupplier.jsp">
				<jsp:param value="${ updateSupplier }" name="updateSupplier"/>
			</jsp:include>
		</div>
	</div>
</div>