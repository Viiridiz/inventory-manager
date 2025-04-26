<%@ page language="java" contentType="text/html; charset=UTF-8"%>
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
  <title>Order Management</title>
  <style>
    body {
      font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Oxygen, Ubuntu, Cantarell, sans-serif;
      line-height: 1.6;
      max-width: 1200px;
      margin: 0 auto;
      padding: 1rem;
    }
    h1, h2 {
      color: #2c3e50;
    }
    table {
      width: 100%;
      border-collapse: collapse;
      margin-top: 1rem;
    }
    th, td {
      padding: 0.75rem;
      border-bottom: 1px solid #ddd;
      text-align: left;
      vertical-align: top;
    }
    th {
      background: #f8f9fa;
    }
    .form-container {
      margin-top: 2rem;
      background: #f8f9fa;
      padding: 1.5rem;
      border-radius: 8px;
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
      border: 1px solid #ccc;
      border-radius: 4px;
    }
    button {
      background: #28a745;
      color: white;
      padding: 0.6rem 1.2rem;
      border: none;
      border-radius: 4px;
      cursor: pointer;
    }
    button:hover {
      background: #218838;
    }
    .small-btn {
      background: #007bff;
      margin-top: 0.5rem;
      padding: 0.3rem 0.7rem;
      font-size: 14px;
    }
    .small-btn:hover {
      background: #0062cc;
    }
  </style>
</head>
<body>

<div style="background: #f8f9fa; padding: 1rem; margin-bottom: 2rem; border-radius: 8px;">
  <a href="${pageContext.request.contextPath}/inventory" style="margin-right: 1rem; text-decoration: none; font-weight: bold; color: #333;">📦 Inventory</a>
  <a href="${pageContext.request.contextPath}/order" style="margin-right: 1rem; text-decoration: none; font-weight: bold; color: #333;">🧾 Orders</a>
</div>

<h1>📦 Order Management</h1>

<!-- Existing Orders Section -->
<h2>Existing Orders 🧾</h2>

<c:choose>
  <c:when test="${empty orders}">
    <p>No orders have been placed yet.</p>
  </c:when>
  <c:otherwise>
    <table>
      <thead>
      <tr>
        <th>Order ID</th>
        <th>Supplier Name</th>
        <th>Products Ordered</th>
        <th>Status</th>
        <th>Order Date</th>
        <th>Action</th>
      </tr>
      </thead>
      <tbody>
      <c:forEach var="order" items="${orders}">
        <tr>
          <td>${order.orderId}</td>
          <td>${order.supplier.name}</td>
          <td>
            <c:choose>
              <c:when test="${not empty order.orderedItems}">
                <ul>
                  <c:forEach var="item" items="${order.orderedItems}">
                    <li>${item.product.name} - ${item.currentStock} units</li>
                  </c:forEach>
                </ul>
              </c:when>
              <c:otherwise>
                No products ordered
              </c:otherwise>
            </c:choose>
          </td>
          <td>${order.status}</td>
          <td>${order.orderDate}</td>
          <td>
            <c:if test="${order.status == 'Pending'}">
              <form action="${pageContext.request.contextPath}/order" method="post">
                <input type="hidden" name="action" value="completeOrder">
                <input type="hidden" name="orderId" value="${order.orderId}">
                <button type="submit" class="small-btn">Mark Completed ✅</button>
              </form>
            </c:if>
          </td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </c:otherwise>
</c:choose>

<!-- Available Products Section -->
<h2>Available Products 🛒</h2>

<c:choose>
  <c:when test="${empty products}">
    <p>No products available. Please add products first.</p>
  </c:when>
  <c:otherwise>
    <table>
      <thead>
      <tr>
        <th>Product ID</th>
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
          <td>${product.name}</td>
          <td>${product.sku}</td>
          <td>${product.category}</td>
          <td>$${product.price}</td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </c:otherwise>
</c:choose>

<!-- Place New Order Form -->
<div class="form-container">
  <h2>Place a New Order 📝</h2>
  <form action="${pageContext.request.contextPath}/order" method="post">

    <div class="form-group">
      <label for="supplierId">Select Supplier:</label>
      <select id="supplierId" name="supplierId" required>
        <option value="">-- Select Supplier --</option>
        <c:forEach var="supplier" items="${suppliers}">
          <option value="${supplier.supplierId}">${supplier.name}</option>
        </c:forEach>
      </select>
    </div>

    <div class="form-group">
      <label for="productSku">Select Product SKU:</label>
      <select id="productSku" name="productSku" required>
        <option value="">-- Select Product --</option>
        <c:forEach var="product" items="${products}">
          <option value="${product.sku}">${product.sku} - ${product.name}</option>
        </c:forEach>
      </select>
    </div>

    <div class="form-group">
      <label for="quantity">Quantity:</label>
      <input type="number" id="quantity" name="quantity" min="1" required>
    </div>

    <button type="submit">Place Order</button>
  </form>
</div>

</body>
</html>
