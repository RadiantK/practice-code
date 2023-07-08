const apiCommentService = (function () {
	
	// 댓글 등록
	function addComment(param) {
		
		const url = "/api/comments/regist";
		
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
	
	// 댓글목록 조회
	function getComments(param) {
		
		const url = "/api/comments";
		
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
	
	// 댓글 삭제
	function removeComment(param) {
		
		const url = "/api/comments/remove";
		
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
		addComment: addComment,
		getComments: getComments,
		removeComment: removeComment,
	}
})();
