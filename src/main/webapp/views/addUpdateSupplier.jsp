<form method="post" action="${ updateSupplier ? 'ang?mvc=updateSupplier' : 'ang?mvc=addSupplier' }">
  <div class="form-group">
    <label for="idSupplier">ID Supplier</label>
    <input type="number" class="form-control" id="idSupplier" aria-describedby="idHelp" placeholder="This field is automatic, without entering anything." 
    		name="idSupplier" value="${ supplier.idSupplier }" readonly="readonly">
    <small id="idHelp" class="form-text text-muted">ID should to be unique.</small>
  </div>
  
  <div class="form-group">
    <label for="social_reason">Social reason</label>
    <input type="text" class="form-control" id="social_reason" placeholder="Input the social reason or supplier name..."
    		name="social_reason" value="${ supplier.social_reason }" required>
  </div>
  
  <div class="form-group">
    <label for="city">City</label>
    <input type="text" class="form-control" id="city" placeholder="Input city name..." name="city" value="${ supplier.city }" required>
  </div>
  
  <div class="form-group">
    <label for="phone">Phone</label>
    <input type="text" class="form-control" id="phone" placeholder="Input phone number..." name="phone" value="${ supplier.phone }" required>
  </div>
  
  <div class="form-group">
    <label for="email">Email</label>
    <input type="email" class="form-control" id="email" placeholder="Input email..." name="email" value="${ supplier.email }"  required>
  </div>
  
  <button type="submit" class="btn btn-primary btn-block">${ updateSupplier ? 'update SUPPLIER' : 'add SUPPLIER' }</button>
</form>