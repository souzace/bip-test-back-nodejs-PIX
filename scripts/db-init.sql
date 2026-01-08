CREATE TABLE pix_payment (
  id UUID PRIMARY KEY,
  amount NUMERIC,
  status VARCHAR(30),
  created_at TIMESTAMP,
  sender_pix_key VARCHAR(100),
  receiver_pix_key VARCHAR(100)
);
