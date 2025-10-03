Feature: Validate bank tab

  Background: Login into Capium Application
    Given Launch the browser
    And User need to enter the Application Url
    And Click on Capium365 Client login
    When User Enter username
    Then User Click on next button
    And User enter OTP
    And Click on verify account
    And Redirect to365 client

  @BankTab
  Scenario: Validate bank tab
    Given Click on bank tab
    Then Verify bank tab

  @BankTab
  Scenario: Validate bank accounts tab
    Given Click on bank tab
    And Click on expand icon
    Then Validate bank accounts

  @BankTab
  Scenario: Validate add new account
    Given Click on bank tab
    And Click on add new account
    Then Continue with the following steps

  