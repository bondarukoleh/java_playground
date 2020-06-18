Feature:
  Get operations with rest assured.

  Scenario: Get author of the post
    Given I send GET to "posts" endpoint
    And post id is "1"
    Then I should see the post object

  Scenario: Get authors of the post
    Given I send GET to "posts" endpoint
    Then I should see the authors collection

  Scenario: Get post by id
    Given I send GET to "posts" endpoint
    And post id is "1" but called with path params
    Then I should see the post object

  Scenario: Get post with parameters
    Given I send GET to "posts" endpoint
    And query parameters title is "Some Post 2" and author is "Oleh"
    Then I should see the post object

  Scenario: Get with TDD style
    Given Sending GET to "posts" endpoint
    And Post id is "2"
    When GET request is send
    Then I should get post with author "Oleh"