## HELPER

### JWT test
curl -X POST -H "Content-Type: application/json" -d '{"username": "LOGIN", "password":"PASSWORD"}' http://localhost:8180/api/v1/auth
curl -H "Authorization: Bearer PREVIOUS_RESPONSE_TOKEN" http://localhost:8180/api/v1/options


