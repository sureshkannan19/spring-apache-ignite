Feature: Testing notification api's
	
	Background:
	  Given url notificationUrl
	  
	Scenario: Update notification via Multipart
	Given path 'updateDetailsViaMultipartFile'
	And multipart file file = { read: 'notificationFile.csv', filename: 'notificationFile.csv', Content-type: 'multipart/form-data' }
	When method POST
	Then status 200
	* print response
	
	Scenario: Get feed by content
	Given path 'getFeedByContent'
	And param keyword = 'SK'
	When method get
	Then status 200
	* def result = karate.jsonPath(response, "$[?(@.feedId == 4)]");
	* print result
	