import React from 'react';
import { Outlet } from 'react-router-dom';
import Navigation from './Navigation';
import Footer from './Footer';

/**
 * Basislayout mit Navigation oben, Content in der Mitte und Footer unten,
 * dokumentiert im gleichen Stil wie die Backend-Klassen.
 */
const Layout = () => (
    <>
        <Navigation />
        <main style={{ padding: '1rem' }}>
            <Outlet />
        </main>
        <Footer />
    </>
);

export default Layout;
