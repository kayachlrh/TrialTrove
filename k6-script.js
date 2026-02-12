import http from 'k6/http';
import { check } from 'k6';

export const options = {
  vus: 50,
  duration: '10s',
};

export default function () {
  const productId = 2;

  const payload = JSON.stringify({
    memberId: 2,
    phone: '01000000000',
  });

  const params = {
    headers: {
      'Content-Type': 'application/json',
    },
  };

  const res = http.post(
    `http://localhost:8080/api/load-test/apply/${productId}`,
    payload,
    params
  );

  console.log(`STATUS=${res.status}`);


  check(res, {
    'success (200)': (r) => r.status === 200,
    'capacity exceeded (409)': (r) => r.status === 409,
  });
}


