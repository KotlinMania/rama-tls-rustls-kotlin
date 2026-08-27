# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 14/14 (100.0%)
- **Function parity:** 76/79 matched (target 156) — 96.2%
- **Class/type parity:** 18/25 matched (target 64) — 72.0%
- **Combined symbol parity:** 94/104 matched (target 220) — 90.4%
- **Average inline-code cosine:** 0.55 (function body across 12 matched files)
- **Average documentation cosine:** 0.43 (doc text across 12 matched files)
- **Cheat-zeroed Files:** 2
- **Critical Issues:** 9 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. server.tls_stream

- **Target:** `server.TlsStream`
- **Similarity:** 0.27
- **Dependents:** 3
- **Priority Score:** 3001007.2
- **Functions:** 10/10 matched (target 12)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_

### 2. client.connector

- **Target:** `client.Connector`
- **Similarity:** 0.52
- **Dependents:** 0
- **Priority Score:** 52004.9
- **Functions:** 10/12 matched (target 17)
- **Missing functions:** `assert_send`, `assert_sync`
- **Types:** 5/8 matched (target 5)
- **Missing types:** `Service`, `Output`, `Error`
- **Tests:** 0/2 matched
- **Lint issues:** 1

### 3. server.service

- **Target:** `server.Service`
- **Similarity:** 0.73
- **Dependents:** 0
- **Priority Score:** 20502.7
- **Functions:** 2/2 matched
- **Missing functions:** _none_
- **Types:** 1/3 matched (target 1)
- **Missing types:** `Output`, `Error`

### 4. server.acceptor_data

- **Target:** `server.AcceptorData`
- **Similarity:** 0.34
- **Dependents:** 0
- **Priority Score:** 11306.6
- **Functions:** 7/8 matched (target 13)
- **Missing functions:** `get_config`
- **Types:** 5/5 matched (target 9)
- **Missing types:** _none_

### 5. server.layer

- **Target:** `server.Layer`
- **Similarity:** 0.77
- **Dependents:** 0
- **Priority Score:** 10502.3
- **Functions:** 3/3 matched (target 4)
- **Missing functions:** _none_
- **Types:** 1/2 matched (target 1)
- **Missing types:** `Service`

### 6. type_conversion

- **Target:** `ramatlsrustls.TypeConversion`
- **Similarity:** 0.24
- **Dependents:** 0
- **Priority Score:** 10407.6
- **Functions:** 3/3 matched (target 35)
- **Missing functions:** _none_
- **Types:** 0/1 matched (target 18)
- **Missing types:** `Error`
- **Tests:** 1/1 matched

### 7. client.connector_data

- **Target:** `client.ConnectorData`
- **Similarity:** 0.56
- **Dependents:** 0
- **Priority Score:** 1204.4
- **Functions:** 10/10 matched (target 20)
- **Missing functions:** _none_
- **Types:** 2/2 matched (target 4)
- **Missing types:** _none_
- **Lint issues:** 2

### 8. client.tls_stream_auto

- **Target:** `client.TlsStreamAuto`
- **Similarity:** 0.62
- **Dependents:** 0
- **Priority Score:** 1103.8
- **Functions:** 11/11 matched (target 12)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 4)
- **Missing types:** _none_

### 9. client.tls_stream

- **Target:** `client.TlsStream`
- **Similarity:** 0.27
- **Dependents:** 0
- **Priority Score:** 1007.3
- **Functions:** 10/10 matched (target 12)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_

### 10. verify

- **Target:** `ramatlsrustls.Verify`
- **Similarity:** 0.74
- **Dependents:** 0
- **Priority Score:** 702.6
- **Functions:** 6/6 matched (target 9)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 10)
- **Missing types:** _none_

### 11. key_log

- **Target:** `ramatlsrustls.KeyLog`
- **Similarity:** 0.59
- **Dependents:** 0
- **Priority Score:** 604.1
- **Functions:** 4/4 matched (target 11)
- **Missing functions:** _none_
- **Types:** 2/2 matched (target 5)
- **Missing types:** _none_

### 12. lib

- **Target:** `ramatlsrustls.Lib`
- **Similarity:** 1.00
- **Dependents:** 0
- **Priority Score:** 100.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 1/1 matched
- **Missing types:** _none_

### 13. client.mod

- **Target:** `client.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 5)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 2)
- **Missing types:** _none_

### 14. server.mod

- **Target:** `server.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 4)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 2)
- **Missing types:** _none_

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

