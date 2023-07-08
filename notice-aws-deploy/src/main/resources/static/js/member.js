const apiMemberService = (function() {
	
	// 회원 로그인
	function loginMember(param) {
		
		const url = "/api/member/login";
		
		const response = fetch(url, {
			method: 'post',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify(param)
		})
		.then(resp => resp.json())
		.then(data => data)
		.catch(error => alert(error));
		
		return response;
	}
	
	return {
		loginMember: loginMember,
	}
})();
