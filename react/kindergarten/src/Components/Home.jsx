import React, {useEffect, useState} from "react";
import Login from "./Login";
import TeacherService from "../API/TeacherService";
import {Button, Modal} from "react-bootstrap";
import TeacherForm from "./TeacherForm";
import Group from "./Group";
import Loader from "../UI/Loader/Loader";

function Home() {
    const [tokens, setTokens] = useState(null);
    const [showLoginForm, setShowLoginForm] = useState(true);
    const [showUpdateForm, setShowUpdateForm] = useState(false);
    const [teacher, setTeacher] = useState("");
    const [showGroup, setShowGroup] = useState(false);
    const [isLogin, setIsLogin] = useState(false);
    const handleLogin = (data) => {
        setTokens(data);
        setShowLoginForm(false);
        setIsLogin(true);
    };

    useEffect(() => {
        if (isLogin) {
            TeacherService.getTeacher(tokens.access_token).then((data) => {
                setTeacher(data)
            });
        }
    }, [isLogin]);


    const handleDelete = () => {
        TeacherService.delete(tokens, setTokens);
    };

    return (
        <div>
            {teacher ? (
                <div>
                    <div className="elem-info">
                        <h3>Your Name: {teacher.name}</h3>
                        <h3>Your Phone: {teacher.phone}</h3>
                        <h3>Your Skype: {teacher.skype}</h3>
                        <h3>Your Mail: {teacher.email}</h3>
                    </div>
                    <div className="teacher-actions">
                        <Button variant="primary" onClick={() => setShowUpdateForm(true)}>update</Button>{" "}
                        <Button variant="danger" onClick={handleDelete}>delete</Button>
                        <Modal show={showUpdateForm} onHide={setShowUpdateForm}>
                            <TeacherForm teacher={teacher} tokens={tokens} setTokens={setTokens}/>
                        </Modal>
                    </div>
                    <div>
                        {
                            showGroup ?
                                <div>
                                    <Button variant="dark" onClick={() => setShowGroup(false)}>close</Button>
                                    <Group tokens={tokens} setTokens={setTokens}/>
                                </div>
                                : <Button variant="secondary" onClick={() => setShowGroup(true)}>View Group</Button>

                        }
                    </div>
                </div>
            ) : tokens ? (
                <div style={{
                    textAlign: 'center',
                    height: '100vh',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center'
                }}>
                    <Loader/>
                </div>
            ) : (
                <Modal show={showLoginForm} onHide={setShowLoginForm}>
                    <Login setTokens={handleLogin} setModal={setShowLoginForm}/>
                </Modal>
            )}

        </div>
    );
}

export default Home;

