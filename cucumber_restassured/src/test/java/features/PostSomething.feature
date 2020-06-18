Feature: Post scenatios

  Scenario: Posting a post
    Given Posting to a "posts" path
    When I'm posting post with text "This is a new post 1"
    And title is "This is post title 1"
    And author is "Oleh (Me) 1"
    Then I should see created post