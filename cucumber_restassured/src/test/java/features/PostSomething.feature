Feature: Post scenarios

  Scenario: Posting a post
    Given Posting to a "posts" path
    When I'm posting post with text "This is a new post 1"
    And title is "This is post title 1"
    And author is "Oleh (Me) 1"
    Then I should see created post

# what the hell is this, omg...
  Scenario: Posting to a different entities
    Given POST for "profile"
    And message body is
      | profileName | id |
      | Oleh something 3 | 3 |
    When I'm send POST request
    Then I should GET the profile with ID: "3" and check that Profile name is "Oleh something 3"
