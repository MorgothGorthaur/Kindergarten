import {Button, Form} from "react-bootstrap";
import Input from "../UI/Input/Input";
import {useEffect, useState} from "react";
import AdminService from "../API/AdminService";

const AdminForm = ({admin, tokens, setTokens}) => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [phone, setPhone] = useState("");

    useEffect(() => {
        if (admin) {
            setEmail(admin.email);
            setPassword(admin.password);
            setPhone(admin.phone);
        }
    }, [admin]);

    const update = (e) => {
        e.preventDefault();
        AdminService.change(email, password, phone, tokens, setTokens);
    };

    const add = (event) => {
        event.preventDefault();
        AdminService.save(email, password, phone);
    };

    return (
        <Form className="form" onSubmit={admin ? update : add}>
            <Input
                type="text"
                placeholder="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
            />
            <Input
                type="password"
                placeholder="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
            />
            <Input
                type="text"
                placeholder="phone"
                value={phone}
                onChange={(e) => setPhone(e.target.value)}
            />
            <Button type="submit">{admin ? "update" : "add"}</Button>
        </Form>
    );
};

export default AdminForm;