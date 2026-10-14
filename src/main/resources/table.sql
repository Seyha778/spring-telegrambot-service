CREATE TABLE dgtl_backend.subscriptions (
                                            id BIGSERIAL PRIMARY KEY,
                                            telegram_user_id BIGINT NOT NULL UNIQUE,
                                            chat_id BIGINT NOT NULL,
                                            username VARCHAR(100),
                                            first_name VARCHAR(100),
                                            subscribed BOOLEAN NOT NULL DEFAULT TRUE,
                                            created_on TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                            updated_on TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_subscriptions_subscribed
    ON dgtl_backend.subscriptions (subscribed);
