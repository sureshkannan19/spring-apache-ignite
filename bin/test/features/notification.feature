Feature: Testing notification api's

	Scenario: Get feed by content
	* url notificationUrl
	Given path 'getFeedByContent'
	And param keyword = 'CR'
	When method get
	Then status 200
	* print response
	