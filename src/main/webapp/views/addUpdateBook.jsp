<form method="post" action="${ updateBook ? 'ang?mvc=updateBook' : 'ang?mvc=addBook' }">
  <div class="form-group">
    <label for="idBook">idBook</label>
    <input type="number" class="form-control" id="idBook" aria-describedby="idHelp" placeholder="This field is automatic, without entering anything." name="idBook" value="${ book.idBook }" readonly="readonly">
    <small id="idHelp" class="form-text text-muted">The idBook should to be unique.</small>
  </div>
  
  <div class="form-group">
    <label for="isbn">ISBN</label>
    <input type="text" class="form-control" id="isbn" placeholder="Input the Book's ISBN" name="isbn" value="${ book.isbn }" required>
  </div>
  
  <div class="form-group">
    <label for="titre">Title</label>
    <input type="text" class="form-control" id="titre" placeholder="Input the title..." name="title" value="${ book.title }" required>
  </div>
  
  <div class="form-group">
    <label for="theme">Theme</label>
    <input type="text" class="form-control" id="theme" placeholder="Input the genre..." name="theme" value="${ book.theme }" required>
  </div>
  
  <div class="form-group">
    <label for="pages">Number of pages</label>
    <input type="number" class="form-control" id="pages" placeholder="Input the number of pages..." name="pages" value="${ book.nbr_pages }" required>
  </div>
  
  <div class="form-group">
    <label for="author">Author</label>
    <input type="text" class="form-control" id="author" placeholder="Input the author's name..." name="author" value="${ book.author }"  required>
  </div>
  
  <div class="form-group">
    <label for="editor">Editor</label>
    <input type="text" class="form-control" id="editor" placeholder="Input the editor's name..." name="editor" value="${ book.editor }"  required>
  </div>
  
  <div class="form-group">
    <label for="price">Price</label>
    <input type="number" class="form-control" id="price" placeholder="Input the price" name="price" value="${ book.price }"  required>
  </div>
  
  <button type="submit" class="btn btn-primary btn-block" >${ updateBook ? 'update BOOK' : 'add BOOK' }</button>
</form>