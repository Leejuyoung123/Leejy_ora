<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="../include/header.jsp"%>


<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<div class="content-header">
		<div class="container-fluid">
			<div class="row mb-2">
				<div class="col-sm-6">
					<h1 class="m-0 text-dark">
						DashBoard Management <small>priview</small>
					</h1>
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


			<div class="col-12">
				<!-- general form elements disabled -->
				<div class="card card-primary">
					<div class="card-header" style="padding-top: 0px;">
						<h3 class="card-title"></h3>
					</div>
					<!-- /.card-header -->
					<div class="card-body">
						<form role="form" action="/admin/board/update" method="post" encType="multipart/form-data">
							<div class="row">
								<div class="col-sm-12">
									<!-- text input -->
									<div class="form-group">
										<h3>UPDATE Board</h3>
										<div class="form-group">
											<label for="exampleTitle">Title</label> <input  value="${boardVO.title}" type="text"
												name="title" class="form-control" id="exampleTitle"
												placeholder="Enter Title">
										</div>
										<div class="form-group">
											<label for="exampleTextarea">Content</label>
											<textarea name="content" class="form-control"
												id="exampleTextarea" rows="3" placeholder="Enter ..">${boardVO.content}</textarea>
										</div>
										<div class="form-group">
											<label for="exampleWriter">Writer</label> <input type="text"
												value="${boardVO.writer}" name="writer" class="form-control" id="exampleWriter"
												placeholder="Enter Writer">
										</div>
										<div class="form-group">
										<td><input type="file" value="파일 선택" name="file" /></td>
										</div>
										
									</div>
									<!-- <form group> -->
									<br>
								</div>
								<!-- col-12- -->
							</div>
							<!-- row -->
							<hr>
							<br>
							<hr>
							<input type="hidden" name="bno" value="${boardVO.bno}"\>
							<input type="hidden" name="page" value="${pageVO.page}"\>
							<button type="submit" class="btn btn-warning">Submit</button>
							<a href="/admin/board/list?page=${pagVO.page}" class="btn btn-primary">List All</a>
						</form>
					</div>
					<!-- card body -->
				</div>
				<!-- card -->
			</div>
			<!--  col-12- -->
		</div>
		<!--  content fuild -->
	</div>
	<!-- content header -->
</div>
<!-- content wrapper -->
<%@include file="../include/footer.jsp"%>