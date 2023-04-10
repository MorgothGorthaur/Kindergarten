import './App.css';
import {Route, Routes} from 'react-router-dom';
import Teachers from "./Components/Teachers";
import {Button, Modal} from "react-bootstrap";
import {useState} from "react";
import Home from './Components/Home';
import TeacherForm from "./Components/TeacherForm";
import AdminForm from "./Components/AdminForm";
import AdminHome from "./Components/AdminHome";

function App() {
    const [showForm, setShowForm] = useState(false);
    const [showAdminForm, setShowAdminForm] = useState(false);

    return (
        <div className="App">
            <header className="App-header">
                <div className="nav-links">
                    <a href="/">main</a>
                    <a href="/teachers">teachers</a>
                    <a href="/home">home</a>
                    <a href="/admin">admin</a>
                </div>
            </header>
            <Routes>
                <Route path="/" element={
                    <div>
                        <h1>Main Page</h1>
                        <Button onClick={() => setShowForm(true)}>add teacher</Button>
                        <Modal show={showForm} onHide={setShowForm}>
                            <TeacherForm/>
                        </Modal>

                        <Button onClick={() => setShowAdminForm(true)}>add admin</Button>
                        <Modal show={showAdminForm} onHide={setShowAdminForm}>
                            <AdminForm/>
                        </Modal>
                    </div>
                }/>
                <Route path="/teachers" element={<Teachers/>}/>
                <Route path="/home" element={<Home/>}/>
                <Route path="/admin" element={<AdminHome/>}/>
            </Routes>
        </div>
    );
}

export default App;