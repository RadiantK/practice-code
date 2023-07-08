const loginService = {
	
	// 초기화
	init: function() {
		
		this.eventInit();
	},
	
	// 이벤트 처리
	eventInit: function() {
		
		const $act = document.querySelectorAll('[data-act]');
		
		$act.forEach(e => {
			e.addEventListener('click', loginService.action);
			e.addEventListener('keyup', loginService.action)
		});
	},
	
	// 이벤트 처리
	action: function(e) {
		
		eType = e.type; // 이벤트 타입
		
		eTarget = e.currentTarget; // 이벤트가 걸린 객체

		actValue = eTarget.getAttribute('data-act'); // data 값
		
		if (eType === "click") {
			if (actValue === "clickLogin") {
				loginService.loginCheck();
			}
		} else if (eType === "keyup") {
			if (actValue === "inputLogin") {
				if (e.keyCode === 13) {
					loginService.loginCheck();
				}
			}
		}
	},
	
	// 회원 로그인 체크
	loginCheck: function() {
		
		const formEl = document.querySelector('.login-form');
		const idEl = formEl.querySelector('#id').value;
		const passwordEl = formEl.querySelector('#password').value;
		
		if (idEl.value === "" || passwordEl === "") {
			alert('아이디 또는 비밀번호를 입력하세요.');
			return;
		}
		
		const param = {
			id: idEl,
			password: passwordEl
		}

		const url = "/api/member/login";
		
		loginService.login(url, param);
	},
	
	// 로그인 api 처리
	login: async function(url, param) {
		
		const response = await fetch(url, {
			method: 'post',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify(param)
		})
		.then(resp => resp.json())
		.then(data => data)
		.catch(error => alert(error));
		
		if (response.data !== 'ok') {
			alert('아이디 또는 비밀번호가 일치하지 않습니다.');
			return;
		}
		
		// 로그인 전 페이지로 리다이렉트
		const currURL = new URL(location.href);
		const searchParam = currURL.searchParams;
		const redicretURL = searchParam.get("redirectURL");
		if (redicretURL != null) {
			location.href = redicretURL;
			return;
		}
		
		location.href = "/";
	}
}

loginService.init();
