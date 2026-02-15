/*
 Find the total successful transaction volume and the number of transactions for each merchant in the year 2025.
 Include the merchant's name and ID.
 Only include merchants who have processed more than €10,000 in total
 */
 SELECT
     m.id,
     m.name,
     SUM(t.amount) as total_amount,
     COUNT(t.id) as transaction_count
 FROM merchants m
 JOIN transactions t
    ON m.id = t.merchant_id
 WHERE t.status = 'SUCCESS'
    AND t.created_at >= '2025-01-01'
    AND t.created_at < '2026-01-01'
 GROUP BY t.id, m.name
 HAVING SUM(t.amount) > 10000