Feature: Dashboard Chart Validation

  Background: Login into Capium Application
    Given Launch the browser
    And User need to enter the Application Url
    And Click on Capium365 Client login
    When User Enter username
    Then User Click on next button
    And User enter OTP
    And Click on verify account
    And Redirect to365 client

  #Invoices Chart-------------------------------------------------
  @Dashboard
  Scenario: Validate Invoice dashboard chart for 'Time Filter'
    When the user selects This Year from the filter dropdown
    Then the dashboard metrics should be visible
    And the chart should display month-wise labels
    When the user selects This Month from the filter dropdown
    Then the dashboard metrics should be visible
    And the chart should display day-wise labels
    When the user selects This Week from the filter dropdown
    Then the dashboard metrics should be visible
    And the chart should display day-wise labels
    When the user selects Last Week from the filter dropdown
    Then the dashboard metrics should be visible
    And the chart should display day-wise labels
    When the user selects Last Month from the filter dropdown
    Then the dashboard metrics should be visible
    And the chart should display day-wise labels
    When the user selects This Quarter from the filter dropdown
    Then the dashboard metrics should be visible
    And the chart should display month-wise labels
    When the user selects Last Quarter from the filter dropdown
    Then the dashboard metrics should be visible
    And the chart should display month-wise labels
    When the user selects Last Year from the filter dropdown
    Then the dashboard metrics should be visible
    And the chart should display daily labels for that month
    When the user selects Custom from the filter dropdown
    Then the user should be prompted to select a date range
    And the dashboard metrics should update after range selection
    When the user selects This Financial Year from the filter dropdown
    Then the dashboard metrics should be visible
    And the chart should display month-wise labels (Apr to Mar)

  #Transactions Chart---------------------------------------------------------
  @Dashboard
  Scenario: Validate Transactions dashboard chart for 'This Year'
    When the user selects This Year from the Transactions filter dropdown
    Then the dashboard metrics should be visible
    And the chart should display month-wise labels
    When the user selects This Month from the Transactions filter dropdown
    Then the dashboard metrics should be visible
    And the chart should display day-wise labels
    When the user selects This Week from the Transactions filter dropdown
    Then the dashboard metrics should be visible
    And the chart should display day-wise labels
    When the user selects Last Week from the Transactions filter dropdown
    Then the dashboard metrics should be visible
    And the chart should display day-wise labels
    When the user selects Last Month from the Transactions filter dropdown
    Then the dashboard metrics should be visible
    And the chart should display day-wise labels
    When the user selects This Quarter from the Transactions filter dropdown
    Then the dashboard metrics should be visible
    And the chart should display month-wise labels
    When the user selects Last Quarter from the Transactions filter dropdown
    Then the dashboard metrics should be visible
    And the chart should display month-wise labels
    When the user selects Last Year from the Transactions filter dropdown
    Then the dashboard metrics should be visible
    And the chart should display daily labels for that month
    When the user selects Custom from the Transactions filter dropdown
    Then the user should be prompted to select a date range
    And the dashboard metrics should update after range selection
    When the user selects This Financial Year from the Transactions filter dropdown
    Then the dashboard metrics should be visible
    And the chart should display month-wise labels (Apr to Mar)
    
    
   
  
