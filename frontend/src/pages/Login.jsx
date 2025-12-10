import React from 'react';
import LoginForm from '../components/LoginForm';

/**
 * Frontend-Seite: Login-Shell um das Formular, dokumentiert im
 * gleichen Stil wie die Backend-Klassenkommentare.
 */
const Login = () => (
    <div style={{ maxWidth: 400, margin: '2rem auto' }}>
        <h2>Login</h2>
        <LoginForm />
    </div>
);

export default Login;
