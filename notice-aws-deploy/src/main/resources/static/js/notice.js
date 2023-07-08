let apiNoticeService = (function() {
	
	// 게시글 등록
	function regist(param) {
		
		const url = "/api/notices/new";
		
		const response = fetch(url, {
			method: 'post',
			headers: {
				
			},
			body: param
		})
		.then(resp => resp.json())
		.then(data => data)
		.catch(error => alert(error));
		
		return response;
	}
	
	// 게시글 목록
	function getNoticeList(param) {
		
		const url = "/api/notices";
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
	
	// 게시글 조회
	function getNotice(param) {
		
		const url = "/api/notices/detail";
		
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
	
	// 게시글 수정
	function editNotice(param) {
		
		const url = "/api/notices/edit";
		
		const response = fetch(url, {
			method: 'post',
			headers: {
				
			},
			body: param
		})
		.then(resp => resp.json())
		.then(data => data)
		.catch(error => alert(error));
		
		return response;
	}
	
	// 게시글 삭제
	function removeNotice(param) {
		
		const url = "/api/notices/remove";
		
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
		regist: regist,
		getNoticeList: getNoticeList,
		getNotice: getNotice,
		editNotice: editNotice,
		removeNotice: removeNotice
	}
})();

export default apiNoticeService;