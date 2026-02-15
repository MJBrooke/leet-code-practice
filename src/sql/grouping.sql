/*
 1. Find the total successful transaction volume and the number of transactions for each merchant in the year 2025.
    Include the merchant's name and ID.
    Only include merchants who have processed more than €20 in total
 */
SELECT m.id,
       m.name,
       SUM(t.amount) AS total_amount,
       COUNT(t.id)   AS transaction_count
FROM merchants m
JOIN transactions t ON m.id = t.merchant_id
WHERE t.status = 'SUCCESS'
  AND t.created_at >= '2025-01-01'
  AND t.created_at < '2026-01-01'
GROUP BY m.id, m.name
HAVING SUM(t.amount) > 20;

/*
 2. Remove duplicate transactions from the transaction volume.
    A potential duplicate is defined as a transaction with the same merchant_id, amount, and currency
        that occurred within 60 seconds of another transaction by that same merchant.
 */
WITH cleaned_transactions AS (
    SELECT
        *,
        ROW_NUMBER() OVER (
            PARTITION BY merchant_id, amount, currency
            ORDER BY created_at
        ) AS occurrence_rank
    FROM transactions
    WHERE status = 'SUCCESS'
)
SELECT
    m.id,
    m.name,
FROM merchants m
JOIN cleaned_transactions t ON m.id = t.merchant_id
WHERE t.occurrence_rank = 1
    AND t.created_at >= '2025-01-01'
    AND t.created_at < '2026-01-01'
GROUP BY m.id, m.name
HAVING SUM(t.amount) > 20;