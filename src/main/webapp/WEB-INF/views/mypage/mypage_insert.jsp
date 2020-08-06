<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="../include/header.jsp"%>
<!-- container start -->
<div id="container">
	<!-- location_area -->
	<div class="location_area member">
		<div class="box_inner">
			<h2 class="tit_page">
				스프링 <span class="in">in</span> 자바
			</h2>
			<p class="location">
				MEMBER <span class="path">/</span> 회원등록
			</p>
			<ul class="page_menu clear">
				<li><a href="javascript:;" class="on">회원등록</a></li>
			</ul>
		</div>
	</div>
	<!-- //location_area -->

	<!-- bodytext_area -->
	<div class="bodytext_area box_inner">

		<!-- bodytext_area -->
		<div class="bodytext_area box_inner">
			<!-- myinfo -->
			<dl class="myinfo">
				<dt>내 정보</dt>
				<dd>
					<!-- appForm -->
					<form action="/mypage/insert" class="regForm" method="post">
						<fieldset>
							<legend>내정보 입력 양식</legend>
							<ul class="reg_list">
								<li class="clear"><span class="tit_lbl  user_id">아이디</span>
									<div class="reg_content" style="padding-top: 8px;">
										<input required id="user_id" value="${memberVO.user_id}" name="user_id"
											type="text" class="w100p" placeholder="아이디를 입력해주세요" />
										<span id="validation_idcheck"></span>
									</div> 
									
								</li>
								<li class="clear"><label class="tit_lbl">패스워드</label>
									<div class="reg_content" style="padding-top: 8px;">
										<input value="" name="user_pw" type="password" class="w100p"
											id="user_pw_lbl " placeholder="비밀번호를 입력해주세요" />
									</div></li>
								<li class="clear"><label class="tit_lbl">이름</label>
									<div class="reg_content" style="padding-top: 8px;">
										<input required value="${memberVO.user_name}" name="user_name"
											type="text" class="w100p" placeholder="이름을 입력해주세요" />
									</div></li>
								<li class="clear"><label class="tit_lbl">이메일</label>
									<div class="reg_content" style="padding-top: 8px;">
										<input value="${memberVO.email}" name="email" type="text"
											class="w100p" placeholder="이메일을 입력해주세요" />
									</div></li>

								<input value="0" name="point" type="hidden" class="w100p"
									placeholder="포인트를 입력해주세요" />
								<input value="1" name="enabled" type="hidden" class="w100p"
									placeholder="포인트를 입력해주세요" />
								<input value="ROLE_USER" name="levels" type="hidden"
									class="w100p" placeholder="포인트를 입력해주세요" />

							</ul>
							<p class="btn_line">
								<button disabled id="btn_submit" class="btn_baseColor" style="cursor:cursor; opacity:0.5">회원가입</button>
							</p>
						</fieldset>
					</form>
					<!-- //appForm -->
				</dd>
			</dl>
			<!-- //myinfo -->

		</div>
		<!-- //bodytext_area -->

	</div>
	<!-- //container -->
</div>
<!-- //bodytext_area -->
<script>
	$(document).ready(function() {
		$("#user_id").blur(function() {
			var user_id = $(this).val();
			$.ajax({
				type : 'get',
				url : '/admin/member/idcheck?user_id=' + user_id,
				// 스프링 시큐리티에서 회원가입할떄 ID중복값을 체크하는 권한이 생김 그경로로 매핑을잡음		
				success : function(result) {
					// 중복 아이디가 존재할떄	
					
					if (result == '1') {// ajax 는 다 String 지정해야함 
						$("#btn_submit").attr("disabled",true);
						$("#btn_submit").css({"cursor":"cursor" ,"opactiy":"0.5"});
						$("#validation_idcheck").text("중복된 아이디가 존재합니다.");
						$("#validation_idcheck").css({"color":"red","font-size":"14px"});
					}else{ 
						$("#btn_submit").attr("disabled",false);
						$("#btn_submit").css({"cursor":"pointer" ,"opactiy":"1"});
						$("#validation_idcheck").text("사용가능한 아이디 입니다.");
						$("#validation_idcheck").css({"color":"red","font-size":"14px"});
						//중복 아이디가 존재하지 않을떄	
					}
				},
						error:function(){
								aleft("아이디 체크 RestAPI 서버가 작동하지 않습니다.");
						}
			});
		});
	});
</script>
<%@ include file="../include/footer.jsp"%>