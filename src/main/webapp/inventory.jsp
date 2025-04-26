<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${not empty sessionScope.flashMessage}">
  <div style="background-color:#d4edda; color:#155724; padding:10px; border-radius:5px; margin-bottom:15px;">
      ${sessionScope.flashMessage}
  </div>
  <c:remove var="flashMessage" scope="session"/>
</c:if>


<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Inventory Manager</title>
  <style>
    body {
      font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Oxygen, Ubuntu, Cantarell, sans-serif;
      line-height: 1.6;
      color: #333;
      max-width: 1200px;
      margin: 0 auto;
      padding: 1rem;
    }
    h1, h2, h3 {
      color: #2c3e50;
      margin-top: 2rem;
    }
    .container {
      display: flex;
      flex-wrap: wrap;
      gap: 2rem;
    }
    .card {
      background: #f8f9fa;
      border-radius: 8px;
      padding: 1.5rem;
      border: 1px solid #e9ecef;
      flex: 1;
      min-width: 300px;
    }
    .form-group {
      margin-bottom: 1rem;
    }
    label {
      display: block;
      margin-bottom: 0.5rem;
      font-weight: bold;
    }
    input, select {
      width: 100%;
      padding: 0.5rem;
      border: 1px solid #ced4da;
      border-radius: 4px;
      font-size: 16px;
    }
    button {
      background: #28a745;
      color: white;
      border: none;
      padding: 0.5rem 1rem;
      border-radius: 4px;
      cursor: pointer;
      font-size: 16px;
    }
    button:hover {
      background: #218838;
    }
    table {
      width: 100%;
      border-collapse: collapse;
      margin-top: 1rem;
    }
    th, td {
      padding: 0.75rem;
      text-align: left;
      border-bottom: 1px solid #e9ecef;
    }
    th {
      background-color: #f8f9fa;
    }
    .tip {
      background: #e3f2fd;
      padding: 1rem;
      border-radius: 4px;
      margin: 1rem 0;
    }
  </style>
</head>
<body>
<div style="background: #f8f9fa; padding: 1rem; margin-bottom: 2rem; border-radius: 8px;">
  <a href="${pageContext.request.contextPath}/inventory" style="margin-right: 1rem; text-decoration: none; font-weight: bold; color: #333;">📦 Inventory</a>
  <a href="${pageContext.request.contextPath}/order" style="margin-right: 1rem; text-decoration: none; font-weight: bold; color: #333;">🧾 Orders</a>
</div>


<h1>Inventory Management System</h1>

<div class="card">
  <h2>System Information</h2>
  <p>Total Products: ${products.size()}</p>
  <p>Total Suppliers: ${suppliers.size()}</p>
  <form action="${pageContext.request.contextPath}/inventory" method="post" style="margin-top: 2rem;">
    <input type="hidden" name="action" value="generateReport">
    <button type="submit">📄 Generate Inventory Report</button>
  </form>
</div>

<!-- List Suppliers -->
<h2>Suppliers 📦</h2>
<c:choose>
  <c:when test="${empty suppliers}">
    <p>No suppliers registered yet.</p>
  </c:when>
  <c:otherwise>
    <table>
      <thead>
      <tr>
        <th>Supplier ID</th>
        <th>Name</th>
        <th>Email</th>
        <th>Phone</th>
      </tr>
      </thead>
      <tbody>
      <c:forEach var="supplier" items="${suppliers}">
        <tr>
          <td>${supplier.supplierId}</td>
          <td>${supplier.name}</td>
          <td>${supplier.contactEmail}</td>
          <td>${supplier.phone}</td>

          <td>
            <form action="${pageContext.request.contextPath}/inventory" method="post" style="display:inline;">
              <input type="hidden" name="action" value="deleteSupplier">
              <input type="hidden" name="supplierId" value="${supplier.supplierId}">
              <button type="submit" style="background:#dc3545; border:none; color:white; padding:5px 10px; border-radius:5px; cursor:pointer;">Delete</button>
            </form>
          </td>

        </tr>
      </c:forEach>
      </tbody>
    </table>
  </c:otherwise>
</c:choose>

<!-- List Products -->
<h2>Products Available 🛒</h2>
<c:choose>
  <c:when test="${empty products}">
    <p>No products added yet.</p>
  </c:when>
  <c:otherwise>
    <table>
      <thead>
      <tr>
        <th>Product ID</th>
        <th>Quantity</th>
        <th>Name</th>
        <th>SKU</th>
        <th>Category</th>
        <th>Price</th>
      </tr>
      </thead>
      <tbody>
      <c:forEach var="product" items="${products}">
        <tr>
          <td>${product.id}</td>

          <td>
            <c:forEach var="item" items="${inventoryItems}">
              <c:if test="${item.product.id == product.id}">
                ${item.currentStock}
              </c:if>
            </c:forEach>
          </td>

          <td>${product.name}</td>
          <td>${product.sku}</td>
          <td>${product.category}</td>
          <td>$${product.price}</td>

          <td>
            <form action="${pageContext.request.contextPath}/inventory" method="post" style="display:inline;">
              <input type="hidden" name="action" value="deleteProduct">
              <input type="hidden" name="productId" value="${product.id}">
              <button type="submit" style="background:#dc3545; border:none; color:white; padding:5px 10px; border-radius:5px; cursor:pointer;">Delete</button>
            </form>
          </td>

        </tr>
      </c:forEach>
      </tbody>
    </table>
  </c:otherwise>
</c:choose>

<div class="tip">
  <strong>Tip:</strong> You can add new products, suppliers, or update stock below!
</div>

<div class="container">
  <!-- Add Product Form -->
  <div class="card">
    <h2>Add New Product 🆕</h2>
    <form action="${pageContext.request.contextPath}/inventory" method="post">
      <input type="hidden" name="action" value="addProduct">

      <div class="form-group">
        <label for="productName">Product Name:</label>
        <input type="text" id="productName" name="productName" required>
      </div>

      <div class="form-group">
        <label for="productSku">SKU:</label>
        <input type="text" id="productSku" name="productSku" required>
      </div>

      <div class="form-group">
        <label for="productCategory">Category:</label>
        <input type="text" id="productCategory" name="productCategory">
      </div>

      <div class="form-group">
        <label for="productPrice">Price:</label>
        <input type="text" id="productPrice" name="productPrice" required>
      </div>

      <div class="form-group">
        <label for="productDescription">Description:</label>
        <input type="text" id="productDescription" name="productDescription">
      </div>

      <button type="submit">Add Product</button>
    </form>
  </div>

  <!-- Add Supplier Form -->
  <div class="card">
    <h2>Add New Supplier 🧑‍💼</h2>
    <form action="${pageContext.request.contextPath}/inventory" method="post">
      <input type="hidden" name="action" value="addSupplier">

      <div class="form-group">
        <label for="supplierName">Supplier Name:</label>
        <input type="text" id="supplierName" name="supplierName" required>
      </div>

      <div class="form-group">
        <label for="supplierEmail">Email:</label>
        <input type="email" id="supplierEmail" name="supplierEmail" required>
      </div>

      <div class="form-group">
        <label for="supplierPhone">Phone:</label>
        <input type="text" id="supplierPhone" name="supplierPhone" required>
      </div>

      <button type="submit">Add Supplier</button>
    </form>
  </div>

  <!-- Update Stock Form -->
  <div class="card">
    <h2>Update Stock 📈</h2>
    <form action="${pageContext.request.contextPath}/inventory" method="post">
      <input type="hidden" name="action" value="updateStock">

      <div class="form-group">
        <label for="productSkuStock">Product SKU:</label>
        <input type="text" id="productSkuStock" name="productSku" required>
      </div>

      <div class="form-group">
        <label for="quantityChange">Quantity Change (+/-):</label>
        <input type="number" id="quantityChange" name="quantityChange" required>
      </div>

      <button type="submit">Update Stock</button>
    </form>
  </div>
</div>

<c:if test="${not empty sessionScope.inventoryReport}">
  <h2>Generated Inventory Report 📄</h2>
  <pre style="background: #f8f9fa; padding: 1rem; border-radius: 8px;">${sessionScope.inventoryReport}</pre>
</c:if>


</body>
</html>
