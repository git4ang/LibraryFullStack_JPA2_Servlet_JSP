<style>
  .fieldsErrorsOrdered {
    color: red;
  }
</style>

<form method="post" action="${updateOrdered ? 'ang?mvc=updateOrdered' : 'ang?mvc=addOrdered'}">
    <div class="form-group">
        <label for="idOrdered">ID Book</label>
        <input type="number" class="form-control" id="idOrdered" name="idOrdered" value="${ ordered.idOrdered }" readonly="readonly">
    </div>

    <div class="form-group">
        <label for="idBook">ID Book</label>
        <input type="number" class="form-control" id="idBook" name="idBook" value="${ ordered.idBook }" readonly="readonly">
    </div>

    <div class="form-group">
        <label for="idSupplier">ID Supplier</label>
        <input type="number" class="form-control" id="idSupplier" name="idSupplier" value="${ ordered.idSupplier }" readonly="readonly">
    </div>

    <div class="form-group">
        <label for="price">Price</label>
        <input type="number" step="0.0001" class="form-control" id="price" placeholder="Input the price" name="price" value="${ordered.price}" required>
        <input type="${ ! empty fieldsErrorsOrdered['price'] ? 'text' : 'hidden' }" class="form-control fieldsErrorsOrdered" value="${ fieldsErrorsOrdered['price'] }" readonly>
    </div>

    <div class="form-group">
        <label for="copies">Number of copies</label>
        <input type="number" class="form-control" id="copies" placeholder="Input the number of copies" name="copies" value="${ordered.nbr_copies}" required>
        <input type="${ ! empty fieldsErrorsOrdered['copies'] ? 'text' : 'hidden' }" class="form-control fieldsErrorsOrdered" value="${ fieldsErrorsOrdered['copies'] }" readonly>
    </div>

    <button type="submit" class="btn btn-primary btn-block">${updateOrdered ? 'update Ordered' : 'add ORDERED'}</button>
</form>
