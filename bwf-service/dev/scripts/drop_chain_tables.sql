drop table if exists swap_pairs;
drop table if exists swap_start_tx_logs;
drop table if exists swap_finalize_tx_logs;
drop table if exists swaps;
drop table if exists txs;
drop table if exists options;

update options set value = "TenSet" where name = "APP_NAME";
