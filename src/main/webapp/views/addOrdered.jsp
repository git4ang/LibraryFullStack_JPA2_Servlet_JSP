<form method="post" action="ang?mvc=addOrdered">
  <div class="form-group">
	<label for="idBook">ID Book</label>
	<input type="text" class="form-control" id="idBook" name="idBook" value="${ idBook }" readonly="readonly">
  </div>

  <div class="form-group">
	<label for="idSupplier">ID Supplier</label>
	<input type="text" class="form-control" id="idSupplier" name="idSupplier" value="${ idSupplier }" readonly="readonly">
  </div>

  <div class="form-group">
	<label for="datePurchase">Date purchase</label>
	<input type="date" class="form-control" id="datePurchase" placeholder="Input the date purchase" name="datePurchase" required>
  </div>

  <div class="form-group">
	<label for="price">Price</label>
	<input type="number" class="form-control" id="price" placeholder="Input the price" name="price"  required>
  </div>

  <div class="form-group">
	<label for="copies">Number of copies</label>
	<input type="number" class="form-control" id="copies" placeholder="Input the number of copies" name="copies"  required>
  </div>

  <button type="submit" class="btn btn-primary btn-block">add ORDERED</button>
</form>
