Feature:
  Get operations with rest assured.

  Scenario: Get author of the post
    Given I send GET to "/post" endpoint
    And post id is "1"
    Then I should see the post object