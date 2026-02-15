/*
 1. Find the total number of successful transactions and the total number of
    refunds for every merchant in Q1 of 2025 (Jan through March).
    Output the merchant name, successful count, and refund count.
 */

-- Target Skills: LEFT JOIN (to include merchants with 0 refunds), COUNT with CASE

SELECT
    m.id,
    m.name,
    -- SUM + CASE_WHEN is the standard way to count conditionals
    SUM(CASE WHEN t.status = 'SUCCESS' THEN 1 ELSE 0 END) AS success_count,
    SUM(CASE WHEN t.status = 'REFUNDED' THEN 1 ELSE 0 END) AS refund_count
FROM merchants m
-- A LEFT JOIN ensures merchants (LHS) are returned, even if they have no rows on transactions (RHS)
LEFT JOIN transactions t
    ON m.id = t.merchant_id
       -- If a condition references the right table of a LEFT JOIN:
            -- Put it in ON if you want to keep unmatched left rows
            -- Put it in WHERE if you want to remove them
       AND t.created_at >= '2025-01-01'
       AND t.created_at < '2025-04-01'
GROUP BY m.id, m.name;

/*
 2. Using the query above, calculate the Refund Ratio (Refunds / Total Successes).
    Only return merchants who have at least 5 successful transactions and a Refund Ratio greater than 20%.
 */

-- Target Skills: CTEs/Subqueries, Handling Division by Zero (NULLIF), and Threshold Filtering (HAVING or WHERE on the CTE).

WITH status_counts AS (
    SELECT
        m.id,
        m.name,
        -- Prepare the data for aggregate-use in the second SELECT
        SUM(CASE WHEN t.status = 'SUCCESS' THEN 1 ELSE 0 END) AS success_count,
        SUM(CASE WHEN t.status = 'REFUNDED' THEN 1 ELSE 0 END) AS refund_count
    FROM merchants m
    LEFT JOIN transactions t
        ON m.id = t.merchant_id
        AND t.created_at >= '2025-01-01'
        AND t.created_at < '2025-04-01'
    GROUP BY m.id, m.name
    -- We can already omit a large chunk of merchants here before passing to the next query.
    -- Additionally, this means we can assume that success_count is never zero, making the ratio calculation safe.
    HAVING SUM(CASE WHEN t.status = 'SUCCESS' THEN 1 ELSE 0 END) > 5
),
calculated_ratios AS (
    SELECT
        *,
        (refund_count * 1.0 / success_count) AS refund_ratio -- Multiply by 1.0 to convert to a decimal outcome
    FROM status_counts
)
SELECT *
FROM calculated_ratios
WHERE refund_ratio > 0.2;


/*
 3. For those high-risk merchants found in Level 2, show a Running Total of their refund volume (amount) ordered by time.
    For each refund, show:
        - The merchant name
        - The refund amount
        - The cumulative refund amount for that merchant up to that point in time.
 */

-- Target Skills: SUM() OVER (PARTITION BY ... ORDER BY ...) - this is the classic "Running Total" pattern.

-- 1. Count success/refund per merchant
WITH status_counts AS (
    SELECT
        m.id,
        m.name,
        -- Prepare the data for aggregate-use in the second SELECT
        SUM(CASE WHEN t.status = 'SUCCESS' THEN 1 ELSE 0 END) AS success_count,
        SUM(CASE WHEN t.status = 'REFUNDED' THEN 1 ELSE 0 END) AS refund_count
    FROM merchants m
         LEFT JOIN transactions t
               ON m.id = t.merchant_id
                   AND t.created_at >= '2025-01-01'
                   AND t.created_at < '2025-04-01'
    GROUP BY m.id, m.name
    -- We can already omit a large chunk of merchants here before passing to the next query.
    -- Additionally, this means we can assume that success_count is never zero, making the ratio calculation safe.
    HAVING SUM(CASE WHEN t.status = 'SUCCESS' THEN 1 ELSE 0 END) > 5
),
-- Calculate risk ratio from counts
calculated_ratios AS (
 SELECT
     *,
     (refund_count * 1.0 / success_count) AS refund_ratio -- Multiply by 1.0 to convert to a decimal outcome
 FROM status_counts
),
-- Filter down to high-risk merchants with bad refund_ratios
high_risk_merchants AS (
 SELECT *
 FROM calculated_ratios
 WHERE refund_ratio > 0.2
)
-- Join back onto original transactions table with this identified set of high-risk merchants
SELECT
    hrm.name,
    t.amount,
    t.created_at,
    SUM(t.amount) OVER (
        PARTITION BY t.merchant_id
        ORDER BY t.created_at
    )
FROM transactions t
JOIN high_risk_merchants hrm ON t.merchant_id = hrm.id
WHERE t.status = 'REFUNDED'
ORDER BY hrm.name, t.created_at
