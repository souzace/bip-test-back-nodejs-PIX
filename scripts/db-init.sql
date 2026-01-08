CREATE TABLE pix_payment (
  id UUID PRIMARY KEY,
  amount NUMERIC,
  status VARCHAR(30),
  sender_pix_key VARCHAR(100),
  receiver_pix_key VARCHAR(100),
  description VARCHAR(255),
  created_at TIMESTAMP
);
