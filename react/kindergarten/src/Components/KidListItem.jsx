

function KidListItem({ kid, tokens, setTokens}) {

    return (
        <div>
            <li className="kids-list-item" key={kid.id}>
                <span className="kid-name">{kid.name}</span>
                <span className="birth-year">({kid.birthYear})</span>
                <div className="button-container">
                    <Button variant="primary" onClick={() => onUpdate(kid)}>
                        update
                    </Button>
                    <Button variant="danger" onClick={() => onDelete(kid.id)}>
                        delete
                    </Button>
                    <Button variant="primary" onClick={() => onShowBrothersAndSisters(kid)}>
                        brothers and sisters
                    </Button>
                    <Button variant="primary" onClick={handleRelative}>
                        {showRelatives ? "close" : "relatives"}
                    </Button>
                </div>
            </li>
            {showRelatives && (
                <div>
                    <Relatives tokens={tokens} setTokens={setTokens} kidId={kid.id} />
                </div>
            )}
        </div>
    );
}

export default KidListItem;