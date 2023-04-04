import React, {useEffect, useState} from "react";
import Login from "./Login";
import AdminService from "../API/AdminService";
import {Button, Modal} from "react-bootstrap";
import AdminForm from "./AdminForm";
import Loader from "../UI/Loader/Loader";
import AdminMenu from "./AdminMenu";

function AdminHome() {
    const [tokens, setTokens] = useState(null);
    const [showLoginForm, setShowLoginForm] = useState(true);
    const [showUpdateForm, setShowUpdateForm] = useState(false);
    const [admin, setAdmin] = useState("");
    const [isLoading, setIsLoading] = useState(false);
    const [isLogin, setIsLogin] = useState(false);

    const handleLogin = (data) => {
        setTokens(data);
        setShowLoginForm(false);
        setIsLogin(true);
    };

    useEffect(() => {
        if (isLogin) {
            setIsLoading(true);
            AdminService.getAdmin(tokens.access_token).then((data) => {
                setAdmin(data);
                setIsLoading(false);
            });
        }
    }, [isLogin]);

    const handleDelete = () => {
        AdminService.delete(tokens, setTokens);
    };

    return (
        <div>
            {admin ? (
                <div className="tile">
                    <div className="elem-info">
                        <h3>Your Email: {admin.email}</h3>
                        <h3>Your Phone: {admin.phone}</h3>
                    </div>
                    <div className="admin-actions">
                        <Button variant="primary" onClick={() => setShowUpdateForm(true)}>
                            update
                        </Button>{" "}
                        <Button variant="danger" onClick={handleDelete}>
                            delete
                        </Button>
                        <Modal show={showUpdateForm} onHide={setShowUpdateForm}>
                            <AdminForm admin={admin} tokens={tokens} setTokens={setTokens}/>
                        </Modal>
                        <AdminMenu tokens={tokens} setTokens={setTokens}/>
                    </div>
                </div>
            ) : isLoading ? (
                <div
                    style={{
                        textAlign: "center",
                        height: "100vh",
                        display: "flex",
                        alignItems: "center",
                        justifyContent: "center",
                    }}
                >
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

export default AdminHome;