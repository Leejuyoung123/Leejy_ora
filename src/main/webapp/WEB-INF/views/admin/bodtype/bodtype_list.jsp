<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ include file="../include/header.jsp"%>
<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<div class="content-header">
		<div class="container-fluid">
			<div class="row mb-2">
				<div class="col-sm-6">
					<h1 class="m-0 text-dark">DashBoard Management</h1>
				</div>
				<!-- /.col -->
				<div class="col-sm-6">
					<ol class="breadcrumb float-sm-right">
						<li class="breadcrumb-item"><a href="#">Home</a></li>
						<li class="breadcrumb-item active">Starter Page</li>
					</ol>
				</div>
				<!-- /.col -->
			</div>
			<!-- /.row -->
		</div>
		<!-- /.container-fluid -->
		
</div>
	<div class="col-12">
		<div class="card">
			<div class="card-header">
				<h3 class="card-title">게시판 생성 리스트</h3>

				<div class="card-tools">
					<div class="input-group input-group-sm" style="width: 150px;">
						<input type="text" name="table_search"
							class="form-control float-right" placeholder="Search">

						<div class="input-group-append">
							<button type="submit" class="btn btn-default">
								<i class="fas fa-search"></i>
							</button>
						</div>
					</div>
				</div>
			</div>
			<!-- /.card-header -->
			<div class="card-body table-responsive p-0">
				<table class="table table-hover text-nowrap">
					<thead>
						<tr>
							<th>bod_sun</th>
							<th>bod_type</th>
							<th>bod_name</th>
					</thead>
					<tbody>
						<c:forEach items="${boardTypeList}" var="boardTypeVO" varStatus="status">
							<tr>
								<td>${boardTypeVO.bod_sun}</td>
								<td><a
									href="/admin/bodtype/update?bod_type=${boardTypeVO.bod_type}">${boardTypeVO.bod_type}</a></td>
								<td>${boardTypeVO.bod_name}</td>
							</tr>
						</c:forEach>
					</tbody>
					<td>	 <a href="/admin/bodtype/write" class="btn btn-primary">게시판 생성</a> </td>
				
				</table>
			</div>
			<!-- /.card-body -->
		</div>
		<!-- /.card -->
	</div>


</div>
<!-- ./Content Wrapper. Contains page content -->
<%@ include file="../include/footer.jsp"%>
