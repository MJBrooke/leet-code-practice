/*
 Occasionally, a merchant's system might accidentally send the same transaction twice.
 Find all "potential duplicates."
 A potential duplicate is defined as a transaction with the same merchant_id, same amount, and same currency
  that occurred within 60 seconds of another transaction by that same merchant.
 */

-- Create a Common Table Expression
-- This is a named, temporary result set used for duration of a single SQL statement.
-- You can add/change/group etc to create a simpler overall query by breaking it down.
WITH transaction_history AS (
    SELECT
        id,
        merchant_id,
        amount,
        currency,
        created_at,
        -- We are adding a column to the original transactions data set
        -- LAG(created_at) says "Give me the created_at value from the previous row"
        -- If there is no previous row, the value will be NULL
        LAG(created_at) OVER (
            -- Key that defines which rows belong together, and therefore what the 'previous row' was
            PARTITION BY merchant_id, amount, currency
            -- Defines the order in the table for comparisons
            ORDER BY created_at
        ) AS prev_created_at -- Create new column called 'prev_created_at'
    FROM transactions
)
SELECT -- This select now uses the result set of the CTE with the additional column available
    id,
    merchant_id,
    amount,
    currency
FROM transaction_history
-- Was this row created within 60 seconds of any prior matching duplicate?
-- This allows multiple transactions with the same merchant_id, currency and amount if it is more than 60 seconds.
WHERE created_at <= prev_created_at + INTERVAL '60 seconds';