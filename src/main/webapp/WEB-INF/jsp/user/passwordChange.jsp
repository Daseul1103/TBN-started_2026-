<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>비밀번호 변경</title>
    <style>
        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            background-color: #f4f6f8;
            font-family: "Noto Sans KR", "Malgun Gothic", sans-serif;
            color: #222;
        }

        .password-change-box {
            width: 100%;
            max-width: 430px;
            padding: 40px;
            margin: 20px;
            background-color: #fff;
            border: 1px solid #e2e6ea;
            border-radius: 10px;
            box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
        }

        .password-change-box h1 {
            margin: 0 0 12px;
            font-size: 26px;
            text-align: center;
        }

        .description {
            margin: 0 0 30px;
            color: #666;
            font-size: 14px;
            line-height: 1.6;
            text-align: center;
        }

        .input-group {
            margin-bottom: 20px;
        }

        .input-group label {
            display: block;
            margin-bottom: 8px;
            font-size: 14px;
            font-weight: 600;
        }

        .input-group input {
            width: 100%;
            height: 48px;
            padding: 0 14px;
            border: 1px solid #cfd5dc;
            border-radius: 6px;
            font-size: 15px;
            outline: none;
        }

        .input-group input:focus {
            border-color: #2563eb;
            box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.12);
        }

        .error-message {
            min-height: 20px;
            margin: -8px 0 14px;
            color: #dc2626;
            font-size: 13px;
        }

        .change-button {
            width: 100%;
            height: 50px;
            border: 0;
            border-radius: 6px;
            background-color: #2563eb;
            color: #fff;
            font-size: 16px;
            font-weight: 700;
            cursor: pointer;
        }

        .change-button:hover {
            background-color: #1d4ed8;
        }
    </style>
</head>
<body>
    <div class="password-change-box">
        <h1>비밀번호 변경</h1>
        <p class="description">
            비밀번호를 변경한 지 90일이 지났습니다.<br>
            새로운 비밀번호를 입력해 주세요.
        </p>

        <form id="passwordChangeForm"
              action="${pageContext.request.contextPath}/user/changePassword.do"
              method="post">
            <div class="input-group">
                <label for="newPassword">변경 비밀번호</label>
                <input type="password"
                       id="newPassword"
                       name="newPassword"
                       placeholder="새 비밀번호를 입력해 주세요"
                       autocomplete="new-password"
                       required>
            </div>

            <div class="input-group">
                <label for="passwordConfirm">변경 비밀번호 확인</label>
                <input type="password"
                       id="passwordConfirm"
                       name="passwordConfirm"
                       placeholder="새 비밀번호를 다시 입력해 주세요"
                       autocomplete="new-password"
                       required>
            </div>

            <p id="errorMessage" class="error-message" aria-live="polite"></p>

            <button type="submit" class="change-button">비밀번호 변경</button>
        </form>
    </div>

    <script>
        const passwordChangeForm = document.getElementById('passwordChangeForm');
        const newPassword = document.getElementById('newPassword');
        const passwordConfirm = document.getElementById('passwordConfirm');
        const errorMessage = document.getElementById('errorMessage');

        function validatePasswords() {
            if (newPassword.value !== passwordConfirm.value) {
                errorMessage.textContent = '변경 비밀번호가 서로 일치하지 않습니다.';
                passwordConfirm.focus();
                return false;
            }

            errorMessage.textContent = '';
            return true;
        }

        passwordConfirm.addEventListener('input', function () {
            if (passwordConfirm.value === '' || newPassword.value === passwordConfirm.value) {
                errorMessage.textContent = '';
            }
        });

        passwordChangeForm.addEventListener('submit', function (event) {
            if (!validatePasswords()) {
                event.preventDefault();
            }
        });
    </script>
</body>
</html>
