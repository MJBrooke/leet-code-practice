/*
 For Merchant A (M_001), calculate the success rate for February 2025 vs. December 2025
 Here, the complexity lies in transforming vertical data (rows of transactions) into horizontal results (comparative rates)
 while handling the "integer division" trap
 */

 -- Create CTE with rows just representing: date_period (2025-02), is_success (1.0 or 0.0)
 WITH prepared_data AS (
     SELECT
         -- Creates a string from the date, so that we can group on it later
         SUBSTR(CAST(created_at AS VARCHAR), 1, 7) AS period,
         -- Add a column of 1 or 0 to be summed together
         -- We use decimal values for the division later
         CASE WHEN status = 'SUCCESS' THEN 1.0 ELSE 0.0 END AS is_success
     FROM transactions
     WHERE merchant_id = 'M_001'
       AND (
            (created_at >= '2025-02-01' AND created_at < '2025-03-01')
         OR (created_at >= '2025-12-01' AND created_at < '2026-01-01')
       )
 )
 SELECT
     period,
     COUNT(*) AS total_txns,
     SUM(is_success) AS success_txns,
     ROUND(
        -- We catch the case where there are no transactions, causing a divide-by-zero with NULLIF
        SUM(is_success) / NULLIF(COUNT(*), 0),
        4 -- Rounding amount
     ) AS success_rate
 FROM prepared_data
 GROUP BY period
 ORDER BY period;